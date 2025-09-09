package com.nhbank.ngw.api.log.controller;

import com.nhbank.ngw.api.log.dto.out.LogEntryDto;
import com.nhbank.ngw.api.log.dto.in.LogQueryRequest;
import com.nhbank.ngw.api.log.dto.out.PageResponse;
import com.nhbank.ngw.common.api.dto.ApiResponse;
import com.nhbank.ngw.common.api.mapper.PageMapper;
import com.nhbank.ngw.domain.log.service.LogService;
import com.nhbank.ngw.common.domain.command.Page;
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
        // 본문이 없으면 기본 요청 생성
        LogQueryRequest safeReq = (logQueryRequest == null) ? new LogQueryRequest() : logQueryRequest;

        // 도메인 커맨드로 변환(내부에서 page/size 기본값 보정)
        var command  = safeReq.toCommand();
        int page = command.page();
        int size = command.size();

        return logService.fetchLogsFromNgw(command)
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(e -> {
                    log.warn("NGW fetch failed: {}", e.getMessage(), e);
                    return Mono.just(Page.empty(page, size));

                })
                .map(result -> PageMapper.toDto(result, LogEntryDto::from))
                .map(ApiResponse::ok);
    }
}