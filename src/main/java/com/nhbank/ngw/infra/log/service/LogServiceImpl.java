package com.nhbank.ngw.infra.log.service;

import com.fasterxml.jackson.core.type.TypeReference;        // ★ 추가
import com.fasterxml.jackson.databind.ObjectMapper;          // ★ 추가
import com.nhbank.ngw.common.config.properties.NgwProperties;
import com.nhbank.ngw.domain.log.command.LogQuery;
import com.nhbank.ngw.domain.log.model.LogEntry;
import com.nhbank.ngw.domain.log.service.LogService;
import com.nhbank.ngw.domain.shared.model.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogServiceImpl implements LogService {

    private final WebClient webClient;
    private final NgwProperties ngwProperties;
    private final ObjectMapper mapper;                      // ★ 추가

    @Override
    public Mono<Page<LogEntry>> fetchLogsFromNgw(LogQuery logQuery) {
        final long readTimeoutMs = (ngwProperties.getTimeout() != null)
                ? ngwProperties.getTimeout().getReadMillis()
                : 10_000; // 기본 10초

        return webClient.post()
                .uri("/api/ngw/outbound/logs")
                .headers(h -> {
                    var key = ngwProperties.getServiceKey();
                    if (key != null && !key.isBlank()) h.add("X-Service-Key", key);
                })
                .bodyValue(logQuery)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), resp ->
                        resp.bodyToMono(byte[].class)
                                .defaultIfEmpty(new byte[0])
                                .map(b -> new String(b, java.nio.charset.StandardCharsets.UTF_8))
                                .doOnNext(body -> log.warn("[NGW] 4xx from /api/ngw/outbound/logs: status={}, body={}",
                                        resp.statusCode(), body))
                                .then(resp.createException())
                )
                .onStatus(status -> status.is5xxServerError(), resp ->
                        resp.bodyToMono(byte[].class)
                                .defaultIfEmpty(new byte[0])
                                .map(b -> new String(b, java.nio.charset.StandardCharsets.UTF_8))
                                .doOnNext(body -> log.error("[NGW] 5xx from /api/ngw/outbound/logs: status={}, body={}",
                                        resp.statusCode(), body))
                                .then(resp.createException())
                )
                // ★ 원문 찍기
                .bodyToMono(String.class)
                .doOnNext(raw -> log.debug("[NGW RAW] status=200 path=/api/ngw/outbound/logs body={}", raw))
                // ★ 문자열 → Page<LogEntry> 파싱
                .flatMap(raw -> Mono.fromCallable(() ->
                        mapper.readValue(raw, new TypeReference<Page<LogEntry>>() {})   // ★ mapper 주입 사용
                ))
                .timeout(Duration.ofMillis(readTimeoutMs))
                .retryWhen(
                        Retry.backoff(2, Duration.ofMillis(300))
                                .filter(t -> true)
                                .onRetryExhaustedThrow((spec, signal) -> signal.failure())
                );
    }
}
