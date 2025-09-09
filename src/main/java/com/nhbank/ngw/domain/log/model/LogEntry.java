package com.nhbank.ngw.domain.log.model;

import lombok.*;

import java.time.LocalDateTime;

@Builder
public record LogEntry(
        Long id,
        LocalDateTime timeTs,   // ← 이 이름으로 유지
        String ngwId,
        String loggerName,
        String logLevel,
        String thread,
        String nodeId,
        String className,
        String guid,
        String message
) {
}
