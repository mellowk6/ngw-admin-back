package com.nhbank.ngw.domain.log.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LogEntry {
    private Long id;
    private LocalDateTime timeTs;   // ← 이 이름으로 유지
    private String ngwId;
    private String loggerName;
    private String logLevel;
    private String thread;
    private String nodeId;
    private String className;
    private String guid;
    private String message;
}
