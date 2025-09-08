package com.nhbank.ngw.api.user.controller;

import com.nhbank.ngw.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(AuthenticationManager authManager,
                          SecurityContextRepository securityContextRepository) {
        this.authManager = authManager;
        this.securityContextRepository = securityContextRepository;
    }

    /** 요청 DTO */
    public record LoginReq(@NotBlank String username, @NotBlank String password) {}

    /** 로그인: DB 기반 AuthenticationManager → SecurityContext 저장 */
    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody LoginReq req,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {
        try {
            // 1) 인증 수행 (UserDetailsService + PasswordEncoder 사용)
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.username(), req.password()));

            // 2) SecurityContext 구성 및 홀더 반영
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);

            // 3) 세션 확보(없으면 생성) + 세션 고정 보호
            HttpSession session = request.getSession(true);
            try { request.changeSessionId(); } catch (IllegalStateException ignore) {}
            session.setAttribute("lastLoginAt", System.currentTimeMillis());

            // 4) 컨텍스트를 세션 저장소에 명시적으로 저장
            securityContextRepository.saveContext(context, request, response);

            return ApiResponse.ok(Map.of("ok", true, "username", auth.getName()));
        } catch (org.springframework.security.core.AuthenticationException ex) {
            // 표준 에러 형식으로 내려가도록 401 던짐 → GlobalExceptionHandler 가 ApiError 로 응답
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호를 확인하세요.");
        }
    }

    /** 로그아웃 */
    @PostMapping("/logout")
    public ApiResponse<Map<String, Object>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        SecurityContextHolder.clearContext();
        return ApiResponse.ok(Map.of("ok", true));
    }
}