package com.nhbank.ngw.api.log.dto.out;

import com.nhbank.ngw.domain.log.model.LogEntry;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public record LogEntryDto(
        String time,
        String ngwId,
        String loggerName,
        String logLevel,
        String thread,
        String nodeId,
        String className,
        String guid,
        String message
) {
    private static final DateTimeFormatter VIEW =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static LogEntryDto from(LogEntry e) {
        return new LogEntryDto(
                e.timeTs() == null ? null
                        : e.timeTs().atZone(ZoneId.of("Asia/Seoul")).format(VIEW),
                e.ngwId(),
                e.loggerName(),
                e.logLevel(),
                e.thread(),
                e.nodeId(),
                e.className(),
                e.guid(),
                e.message()
        );
    }
}
