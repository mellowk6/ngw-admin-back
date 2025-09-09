package com.nhbank.ngw.api.log.controller;

import com.nhbank.ngw.api.log.dto.out.LogEntryDto;
import com.nhbank.ngw.api.log.dto.in.LogQueryRequest;
import com.nhbank.ngw.api.log.dto.out.PageResponse;
import com.nhbank.ngw.common.api.dto.ApiResponse;
import com.nhbank.ngw.common.api.mapper.PageMapper;
import com.nhbank.ngw.domain.log.service.LogService;
import com.nhbank.ngw.domain.shared.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
@Slf4j
public class LogController {

    private final LogService logService;

    @PostMapping
    public Mono<ApiResponse<PageResponse<LogEntryDto>>> list(
            @RequestBody(required = false) LogQueryRequest logQueryRequest
    ) {

        // 본문이 없을 때도 안전하게 처리
        LogQueryRequest safeReq = (logQueryRequest == null) ? LogQueryRequest.builder().build() : logQueryRequest;

        // 기본값 보정 포함하여 도메인 커맨드로 변환
        var cmd  = safeReq.toCommand();
        int page = cmd.page();
        int size = cmd.size();

        return logService.fetchLogsFromNgw(cmd)
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(e -> {
                    log.warn("NGW fetch failed. page={}, size={}, cause={}", page, size, e.toString(), e);
                    return Mono.just(Page.empty(page, size));
                })
                .map(result -> PageMapper.toDto(result, LogEntryDto::from))
                .map(ApiResponse::ok);
    }
}