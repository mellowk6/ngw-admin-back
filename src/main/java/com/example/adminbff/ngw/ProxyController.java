package com.example.adminbff.ngw;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/proxy")
@RequiredArgsConstructor
public class ProxyController {

    private final NgwClient ngwClient;

    @GetMapping("/stats")
    public Mono<String> stats(@RequestParam String group, Authentication auth) {
        return ngwClient.getStats(group);
    }
}
