package com.nhbank.ngw.api.log.controller;

import com.nhbank.ngw.api.log.dto.out.LogEntryDto;
import com.nhbank.ngw.api.log.dto.in.LogQueryRequest;
import com.nhbank.ngw.api.log.dto.out.PageResponse;
import com.nhbank.ngw.common.api.dto.ApiResponse;
import com.nhbank.ngw.domain.log.service.NgwLogProxyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
@Slf4j
public class LogController {

    private final NgwLogProxyService ngw;

    @PostMapping
    public Mono<ApiResponse<PageResponse<LogEntryDto>>> list(
            @RequestBody(required = false) LogQueryRequest req
    ) {
        final int size = (req != null && req.getSize() != null) ? req.getSize() : 10;

        return ngw.fetchLogsFromNgw(req)
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(e -> {
                    log.warn("NGW fetch failed: {}", e.getMessage(), e);
                    // 실패 시에도 UI가 깨지지 않도록 '빈 페이지'를 내려줌
                    var empty = new PageResponse<LogEntryDto>(List.of(), 0, size, 0, 0);
                    return Mono.just(empty);
                })
                .map(ApiResponse::ok);
    }
}