package com.example.adminbff.service;

import com.example.adminbff.dto.LogEntryDto;
import com.example.adminbff.dto.LogQueryRequest;
import com.example.adminbff.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NgwLogProxyService {
    private final WebClient ngwWebClient;

    public Mono<PageResponse<LogEntryDto>> fetchLogsFromNgw(LogQueryRequest req) {
        var safe = (req != null) ? req : new LogQueryRequest();
        return ngwWebClient.post()
                .uri("/api/ngw/outbound/logs")
                .bodyValue(safe)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<LogEntryDto>>() {});
    }
}