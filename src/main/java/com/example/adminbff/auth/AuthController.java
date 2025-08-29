package com.example.adminbff.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(AuthenticationManager authManager,
                          SecurityContextRepository securityContextRepository) {
        this.authManager = authManager;
        this.securityContextRepository = securityContextRepository;
    }

    public record LoginReq(@NotBlank String username, @NotBlank String password) {}

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginReq req,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        // 1) 인증 수행
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));

        // 2) SecurityContext 구성 및 홀더 반영
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // 3) 세션 확보(없으면 생성) + 세션 고정 공격 방지
        HttpSession session = request.getSession(true);
        try { request.changeSessionId(); } catch (IllegalStateException ignore) {}

        // (선택) 마지막 로그인 시각 등 세션 부가 정보
        session.setAttribute("lastLoginAt", System.currentTimeMillis());

        // 4) ★ 컨텍스트를 세션 저장소에 명시적으로 저장 (중요)
        securityContextRepository.saveContext(context, request, response);

        return Map.of("ok", true);
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        SecurityContextHolder.clearContext();
        return Map.of("ok", true);
    }
}