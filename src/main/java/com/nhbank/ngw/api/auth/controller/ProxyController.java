package com.nhbank.ngw.api.auth.controller;

import com.nhbank.ngw.infra.external.NgwClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/proxy")
@RequiredArgsConstructor
public class ProxyController {

    private final NgwClient ngwClient;

    /** NGW 통계 프록시 (예시)
     *  - 인증 필요: SecurityConfig에서 "/proxy/**"는 authenticated()
     *  - GET이므로 CSRF 영향 없음
     */
    @GetMapping("/stats")
    public Mono<String> stats(@RequestParam String group, Authentication auth) {
        // 필요시 auth.getName() 등으로 호출자 식별 로그에 활용 가능
        return ngwClient.getStats(group);
    }
}