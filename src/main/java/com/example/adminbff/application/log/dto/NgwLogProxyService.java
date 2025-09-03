package com.example.adminbff.application.log.dto;

import com.example.adminbff.api.log.dto.LogEntryDto;
import com.example.adminbff.api.log.dto.LogQueryRequest;
import com.example.adminbff.api.log.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
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