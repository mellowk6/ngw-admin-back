package com.example.adminbff.dto;

import com.example.adminbff.entity.LogEntry;

import java.time.Instant;

public record LogEntryDto(
        Instant time, String ngwId, String loggerName, String logLevel,
        String thread, String nodeId, String className, String guid, String message
) {
    public static LogEntryDto from(LogEntry e) { return new LogEntryDto(
            e.getTime(), e.getNgwId(), e.getLoggerName(), e.getLogLevel(),
            e.getThread(), e.getNodeId(), e.getClassName(), e.getGuid(), e.getMessage()
    );}
}