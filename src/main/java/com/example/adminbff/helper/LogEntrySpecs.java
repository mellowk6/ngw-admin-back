package com.example.adminbff.helper;

import com.example.adminbff.entity.LogEntry;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class LogEntrySpecs {
    public static Specification<LogEntry> guidLike(String guid) {
        return (root, q, cb) -> (guid==null || guid.isBlank())
                ? cb.conjunction()
                : cb.like(root.get("guid"), "%"+guid.trim()+"%");
    }
    public static Specification<LogEntry> loggerIn(List<String> logger) {
        return (root, q, cb) -> (logger==null || logger.isEmpty())
                ? cb.conjunction()
                : root.get("loggerName").in(logger);
    }
}