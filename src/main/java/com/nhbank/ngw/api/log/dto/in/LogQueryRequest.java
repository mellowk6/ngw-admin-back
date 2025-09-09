package com.nhbank.ngw.api.log.dto.in;

import com.nhbank.ngw.domain.log.command.LogQuery;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Builder
public record LogQueryRequest(
        String guid,
        String logger,
        Integer page,   // null 허용 (기본값은 서비스에서 보정)
        Integer size,   // null 허용
        Integer limit  // guids 조회용 (없으면 생략 가능)


) {
    public LogQuery toCommand() {
        int p = (page == null || page < 0) ? 0 : page;
        int s = (size == null || size <= 0) ? 10 : Math.min(500, size);
        return new LogQuery(StringUtils.trimToNull(guid), StringUtils.trimToNull(logger), p, s, limit);
    }
}