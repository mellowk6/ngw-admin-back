package com.example.adminbff.dto;

import com.example.adminbff.entity.LogEntry;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public record LogEntryDto(
        String time,         // ← 문자열(ISO-8601)로 통일
        String ngwId,
        String loggerName,
        String logLevel,
        String thread,
        String nodeId,
        String className,
        String guid,
        String message
) {
    private static final DateTimeFormatter ISO =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault());

    // DB 엔티티 → 계약 DTO 변환
    public static LogEntryDto from(LogEntry e) {
        return new LogEntryDto(
                e.getTime() == null ? null : ISO.format(e.getTime()),
                e.getNgwId(), e.getLoggerName(), e.getLogLevel(),
                e.getThread(), e.getNodeId(), e.getClassName(), e.getGuid(), e.getMessage()
        );
    }
}