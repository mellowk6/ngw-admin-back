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
                e.getTimeTs() == null ? null : ISO.format(e.getTimeTs().atZone(ZoneId.systemDefault())),
                e.getNgwId(),
                e.getLoggerName(),
                e.getLogLevel(),
                e.getThread(),
                e.getNodeId(),
                e.getClassName(),
                e.getGuid(),
                e.getMessage()
        );
    }
}
