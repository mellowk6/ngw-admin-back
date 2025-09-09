package com.nhbank.ngw.domain.log.service;

import com.nhbank.ngw.domain.log.model.LogEntry;
import com.nhbank.ngw.domain.log.command.LogQuery;
import com.nhbank.ngw.domain.shared.Page;
import reactor.core.publisher.Mono;

public interface LogService {
    Mono<Page<LogEntry>> fetchLogsFromNgw(LogQuery req);
}
