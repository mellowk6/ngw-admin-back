package com.example.adminbff.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "log_entries")
@Getter
@Setter
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    @Column(nullable=false)
    Instant time;         // 시간
    @Column(name="ngw_id") String ngwId;          // NGW ID
    @Column(name="logger_name") String loggerName;// Logger Name
    @Column(name="log_level") String logLevel;    // Log Level
    String thread;                                // Thread
    @Column(name="node_id") String nodeId;        // NODE ID
    @Column(name="class_name") String className;  // Class
    String guid;                                  // GUID
    @Column(columnDefinition="TEXT") String message; // 메시지
}