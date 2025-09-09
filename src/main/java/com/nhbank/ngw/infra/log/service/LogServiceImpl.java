package com.nhbank.ngw.infra.log.service;

import com.nhbank.ngw.domain.log.model.LogEntry;

import com.nhbank.ngw.common.config.properties.NgwProperties;
import com.nhbank.ngw.domain.log.command.LogQuery;
import com.nhbank.ngw.domain.log.service.LogService;
import com.nhbank.ngw.domain.shared.model.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogServiceImpl implements LogService {

    private final WebClient webClient;
    private final NgwProperties ngwProperties;

    /**
     * NGW 로그 조회 프록시
     * - X-Service-Key 헤더로 NGW 인증
     * - 4xx/5xx 응답 바디를 포함하여 의미 있는 에러로 전달
     * - 읽기 타임아웃 및 5xx 재시도(최대 2회, 지수 백오프)
     */
    public Mono<Page<LogEntry>> fetchLogsFromNgw(LogQuery logQuery) {


        final long readTimeoutMs = (ngwProperties.getTimeout() != null)
                ? ngwProperties.getTimeout().getReadMillis()
                : 10_000; // 기본 10초

        return webClient.post()
                .uri("/api/ngw/outbound/logs")
                .headers(h -> {
                    // 서비스 키 전달 (필요 시 다른 인증 헤더 추가 가능)
                    if (ngwProperties.getServiceKey() != null && !ngwProperties.getServiceKey().isBlank()) {
                        h.add("X-Service-Key", ngwProperties.getServiceKey());
                    }
                })
                .bodyValue(logQuery)
                .retrieve()
                // 4xx -> 클라 입력 오류/권한 오류 등으로 분류
                .onStatus(status -> status.is4xxClientError(), resp ->
                        resp.bodyToMono(byte[].class)
                                .defaultIfEmpty(new byte[0])
                                .map(body -> new String(body, StandardCharsets.UTF_8))
                                .doOnNext(body -> log.warn("[NGW] 4xx from /api/ngw/outbound/logs: status={}, body={}",
                                        resp.statusCode(), body))
                                .then(resp.createException())   // ★ 여기! Mono<? extends Throwable> 그대로 반환
                )
                .onStatus(status -> status.is5xxServerError(), resp ->
                        resp.bodyToMono(byte[].class)
                                .defaultIfEmpty(new byte[0])
                                .map(body -> new String(body, StandardCharsets.UTF_8))
                                .doOnNext(body -> log.error("[NGW] 5xx from /api/ngw/outbound/logs: status={}, body={}",
                                        resp.statusCode(), body))
                                .then(resp.createException())   // ★ 여기도 동일
                )
                .bodyToMono(new ParameterizedTypeReference<Page<LogEntry>>() {
                })
                // 읽기 타임아웃 (커넥터 레벨 readTimeout과 별개로 파이프라인 보호)
                .timeout(Duration.ofMillis(readTimeoutMs))
                // 일시적 장애에 대한 재시도 (5xx/타임아웃 등)
                .retryWhen(
                        Retry.backoff(2, Duration.ofMillis(300))
                                .filter(throwable -> true) // 필요 시 조건 좁히기
                                .onRetryExhaustedThrow((spec, signal) -> signal.failure())
                );
    }
}