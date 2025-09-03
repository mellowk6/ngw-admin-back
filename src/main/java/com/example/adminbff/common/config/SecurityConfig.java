package com.example.adminbff.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    /** 세션 기반 SecurityContext 저장소 */
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    /** 프론트에서 사용하기 쉬운 쿠키 기반 CSRF 저장소를 빈으로 노출 */
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
                                    CsrfTokenRepository csrfRepo) throws Exception {

        http
                // CSRF: 기본 활성. 일부 경로는 예외 처리
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfRepo)
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher("/api/auth/csrf"),
                                new AntPathRequestMatcher("/h2-console/**"),
                                new AntPathRequestMatcher("/actuator/**"),
                                // NGW 로그 프록시 조회는 POST이므로 CSRF 제외
                                new AntPathRequestMatcher("/api/logs/**", "POST")
                        )
                )
                .headers(h -> h.frameOptions(f -> f.sameOrigin())) // H2 콘솔 허용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionFixation(sf -> sf.changeSessionId()) // 세션 고정 보호
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
                                "/actuator/health"
                        ).permitAll()

                        // 로그 프록시 공개(필요시 authenticated()로 바꾸세요)
                        .requestMatchers("/api/logs/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/me").authenticated()
                        .requestMatchers("/proxy/**").authenticated()
                        .anyRequest().denyAll()
                )
                .exceptionHandling(e -> e.authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .formLogin(f -> f.disable())   // 컨트롤러에서 로그인 처리 시 비활성
                .httpBasic(b -> b.disable());  // 기본 인증 비활성

        return http.build();
    }

    /** CORS 설정 */
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

    /** 비밀번호 해시 */
    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    /** 로그인에 사용할 AuthenticationManager (컨트롤러 로그인용) */
    @Bean
    AuthenticationManager authenticationManager(UserDetailsService uds, PasswordEncoder pe) {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(pe);
        return new ProviderManager(provider);
    }

    /** 동시 세션 제어 등에 사용 가능 */
    @Bean
    SessionRegistry sessionRegistry() { return new SessionRegistryImpl(); }
}