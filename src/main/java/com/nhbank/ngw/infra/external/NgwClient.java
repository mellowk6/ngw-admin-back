package com.nhbank.ngw.infra.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class NgwClient {

    private final WebClient wc;

    public NgwClient(
        @Value("${ngw.base-url}") String baseUrl,
        @Value("${ngw.service-key:}") String serviceKey
    ) {
        this.wc = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("X-Service-Key", serviceKey)
            .defaultHeader(HttpHeaders.ACCEPT, "application/json")
            .build();
    }

    public Mono<String> getStats(String group) {
        return wc.get()
            .uri(uri -> uri.path("/internal/stats").queryParam("group", group).build())
            .retrieve()
            .bodyToMono(String.class);
    }
}
