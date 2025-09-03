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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,
                                    SecurityContextRepository contextRepo) throws Exception {

        http
                // CSRF: 기본 활성. 읽기 전용 로그 API(POST)는 예외로 둠.
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher("/api/auth/csrf"),
                                new AntPathRequestMatcher("/h2-console/**"),
                                new AntPathRequestMatcher("/actuator/**"),
                                // ✅ NGW 로그 프록시 API는 POST로 조회하므로 CSRF 제외
                                new AntPathRequestMatcher("/api/logs/**", "POST")
                        )
                )
                .headers(h -> h.frameOptions(f -> f.sameOrigin())) // H2 콘솔
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        // 로그인 후 세션 ID만 교체(세션 데이터/토큰 유지)
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
                                "/actuator/health"
                        ).permitAll()

                        // ✅ 이제 로그 페이지는 NGW 프록시만 호출: /api/logs/**
                        // 필요 시 .authenticated() 로 변경 가능
                        .requestMatchers("/api/logs/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/me").authenticated()
                        .requestMatchers("/proxy/**").authenticated()
                        .anyRequest().denyAll()
                )
                .exceptionHandling(e -> e.authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .formLogin(f -> f.disable())
                .httpBasic(b -> b.disable());

        return http.build();
    }

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

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService uds, PasswordEncoder pe) {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(pe);
        return new ProviderManager(provider);
    }

    @Bean
    SessionRegistry sessionRegistry() { return new SessionRegistryImpl(); }
}