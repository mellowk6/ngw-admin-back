package com.nhbank.ngw.domain.log.service;

import com.nhbank.ngw.api.log.dto.out.LogEntryDto;
import com.nhbank.ngw.api.log.dto.in.LogQueryRequest;
import com.nhbank.ngw.api.log.dto.out.PageResponse;
import reactor.core.publisher.Mono;

public interface NgwLogProxyService {
    Mono<PageResponse<LogEntryDto>> fetchLogsFromNgw(LogQueryRequest req);
}
