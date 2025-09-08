package com.nhbank.ngw.api.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class CsrfController {

    private final CsrfTokenRepository csrfTokenRepository; // 기본 세션 리포지토리 주입(스프링 자동 구성)

    @GetMapping("/csrf")
    public ResponseEntity<Map<String, String>> csrf(HttpServletRequest req, HttpServletResponse res) {
        // 요청에 이미 만들어진 토큰이 있으면 사용, 없으면 강제로 생성/저장
        CsrfToken token = (CsrfToken) req.getAttribute(CsrfToken.class.getName());
        if (token == null) {
            token = csrfTokenRepository.generateToken(req);
            csrfTokenRepository.saveToken(token, req, res);
        }
        return ResponseEntity.ok(Map.of(
                "headerName", token.getHeaderName(),
                "token", token.getToken()
        ));
    }
}