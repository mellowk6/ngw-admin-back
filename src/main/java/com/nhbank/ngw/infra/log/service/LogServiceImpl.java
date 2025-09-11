package com.nhbank.ngw.infra.log.service;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.nhbank.ngw.common.config.properties.NgwProperties;
import com.nhbank.ngw.domain.log.command.LogQuery;
import com.nhbank.ngw.domain.log.model.LogEntry;
import com.nhbank.ngw.domain.log.service.LogService;
import com.nhbank.ngw.domain.shared.model.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogServiceImpl implements LogService {

    private final WebClient webClient;
    private final NgwProperties ngwProperties;

    // NGW 응답 래퍼
    private record NgwPage<T>(List<T> content, int number, int size, long totalElements, int totalPages) {}

    // NGW 로그 아이템: epoch millis를 수신 (time 또는 timeMs 둘 다 허용)
    private record NgwLogItem(
            @JsonAlias({ "time", "timeMs" }) Long timeMs,
            String ngwId,
            String loggerName,
            String logLevel,
            String thread,
            String nodeId,
            String className,
            String guid,
            String message
    ) {}

    @Override
    public Mono<Page<LogEntry>> fetchLogsFromNgw(LogQuery req) {
        final long readTimeoutMs = (ngwProperties.getTimeout() != null)
                ? ngwProperties.getTimeout().getReadMillis()
                : 10_000;

        var type = new ParameterizedTypeReference<NgwPage<NgwLogItem>>() {};

        return webClient.post()
                .uri("/api/ngw/outbound/logs")
                .headers(h -> {
                    var key = ngwProperties.getServiceKey();
                    if (key != null && !key.isBlank()) h.add("X-Service-Key", key);
                })
                .bodyValue(req)
                .retrieve()
                .bodyToMono(type)   // NGW 응답을 바로 역직렬화
                .map(pg -> {
                    var items = pg.content().stream().map(it -> {
                        // Long(ms) → LocalDateTime(서버 기본 TZ)
                        var ldt = (it.timeMs() == null)
                                ? null
                                : Instant.ofEpochMilli(it.timeMs())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();

                        return LogEntry.builder()
                                .timeTs(ldt)
                                .ngwId(it.ngwId())
                                .loggerName(it.loggerName())
                                .logLevel(it.logLevel())
                                .thread(it.thread())
                                .nodeId(it.nodeId())
                                .className(it.className())
                                .guid(it.guid())
                                .message(it.message())
                                .build();
                    }).toList();

                    return Page.of(items, pg.number(), pg.size(), pg.totalElements());
                })
                .timeout(Duration.ofMillis(readTimeoutMs))
                .retryWhen(
                        Retry.backoff(2, Duration.ofMillis(300))
                                .filter(t -> true)
                                .onRetryExhaustedThrow((spec, signal) -> signal.failure())
                );
    }
}
