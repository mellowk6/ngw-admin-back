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
    private static final DateTimeFormatter ISO =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault());

    public static LogEntryDto from(LogEntry e) {
        return new LogEntryDto(
                e.timeTs() == null ? null : ISO.format(e.timeTs().atZone(ZoneId.systemDefault())),
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
