package com.example.adminbff.api.log.controller;

import com.example.adminbff.api.log.dto.LogEntryDto;
import com.example.adminbff.api.log.dto.LogQueryRequest;
import com.example.adminbff.api.log.dto.PageResponse;
import com.example.adminbff.application.log.dto.NgwLogProxyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
@Slf4j
public class LogController {

    private final NgwLogProxyService ngw;

    @PostMapping
    public ResponseEntity<PageResponse<LogEntryDto>> list(@RequestBody(required = false) LogQueryRequest req) {
        try {
            var body = ngw.fetchLogsFromNgw(req).block(Duration.ofSeconds(5));
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            log.warn("NGW fetch failed: {}", e.getMessage(), e);
            // 실패 시 빈 페이지
            return ResponseEntity.ok(new PageResponse<>(java.util.List.of(), 0,
                    req != null && req.getSize() != null ? req.getSize() : 10, 0, 0));
        }
    }
}