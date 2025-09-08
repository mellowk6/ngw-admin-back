package com.nhbank.ngw.common.config;

import com.nhbank.ngw.common.security.Json401EntryPoint;
import com.nhbank.ngw.common.security.InactivityTimeoutFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.List;

@Configuration
public class SecurityConfig {

    /**
     * server.servlet.session.timeout 값을 주입해 필터 빈 생성
     */
    @Bean
    public InactivityTimeoutFilter inactivityTimeoutFilter(
            @Value("${server.servlet.session.timeout}") Duration sessionTimeout) {
        // null 방어 및 최소값 처리(선택)
        Duration effective = (sessionTimeout != null ? sessionTimeout : Duration.ofMinutes(1));
        return new InactivityTimeoutFilter(effective);
    }

    /**
     * 세션 기반 SecurityContext 저장소
     */
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    /**
     * 프론트에서 사용하기 쉬운 쿠키 기반 CSRF 저장소
     */
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repo = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repo.setCookieName("XSRF-TOKEN");
        repo.setHeaderName("X-XSRF-TOKEN");
        repo.setCookiePath("/");
        return repo;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,
                                    SecurityContextRepository contextRepo,
                                    CsrfTokenRepository csrfRepo,
                                    InactivityTimeoutFilter inactivityTimeoutFilter) throws Exception {

        http
                // CSRF
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfRepo)
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher("/api/auth/csrf"),
                                new AntPathRequestMatcher("/h2-console/**"),
                                new AntPathRequestMatcher("/actuator/**"),
                                new AntPathRequestMatcher("/api/logs/**", "POST")
                        )
                )
                .headers(h -> h.frameOptions(f -> f.sameOrigin())) // H2
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionFixation(sf -> sf.changeSessionId())
                )
                .securityContext(sc -> sc.securityContextRepository(contextRepo))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/api/auth/csrf",
                                "/api/auth/login",
                                "/api/auth/logout",
                                "/api/auth/check-id",
                                "/api/auth/signup",
                                "/h2-console/**",
                                "/actuator/health",
                                "/api/dept/list"
                        ).permitAll()
                        .requestMatchers("/api/logs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/me").authenticated()
                        .requestMatchers("/proxy/**").authenticated()
                        .anyRequest().denyAll()
                )
                // 인증 필요하지만 인증없음 → JSON 401
                .exceptionHandling(e -> e.authenticationEntryPoint(new Json401EntryPoint()))
                .formLogin(f -> f.disable())
                .httpBasic(b -> b.disable());

        // ★ 무활동 타임아웃 필터 등록 (SecurityContextHolderFilter 앞)
        http.addFilterBefore(inactivityTimeoutFilter, SecurityContextHolderFilter.class);

        return http.build();
    }

    /**
     * CORS 설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var conf = new CorsConfiguration();
        conf.setAllowedOrigins(List.of("http://localhost:5173", "https://admin.example.com"));
        conf.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        conf.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-XSRF-TOKEN", "X-CSRF-TOKEN"));
        conf.setAllowCredentials(true);
        conf.setMaxAge(3600L);

        var src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", conf);
        return src;
    }

    /**
     * 비밀번호 해시
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 로그인에 사용할 AuthenticationManager (컨트롤러 로그인용)
     */
    @Bean
    AuthenticationManager authenticationManager(UserDetailsService uds, PasswordEncoder pe) {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(pe);
        return new ProviderManager(provider);
    }

    /**
     * 로그아웃 핸들러: 세션 무효화 + 인증정보 제거
     */
    @Bean
    public LogoutHandler logoutHandler() {
        SecurityContextLogoutHandler h = new SecurityContextLogoutHandler();
        h.setInvalidateHttpSession(true);
        h.setClearAuthentication(true);
        return h;
    }

    /**
     * 세션 인증 전략: 세션ID 교체(+ 선택적으로 세션 레지스트리 등록)
     */
    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy(SessionRegistry sessionRegistry) {
        return new CompositeSessionAuthenticationStrategy(List.of(
                new ChangeSessionIdAuthenticationStrategy()
                // 필요 시: new RegisterSessionAuthenticationStrategy(sessionRegistry)
        ));
    }

    /**
     * 동시 세션 제어 등에 사용 가능
     */
    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
