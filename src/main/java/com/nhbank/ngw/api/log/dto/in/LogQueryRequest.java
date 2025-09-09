package com.nhbank.ngw.api.log.dto.in;

import com.nhbank.ngw.domain.log.command.LogQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogQueryRequest {
    private String guid;
    private String logger;
    private Integer page;   // null 허용 (기본값은 서비스에서 보정)
    private Integer size;   // null 허용
    private Integer limit;  // guids 조회용 (없으면 생략 가능)

    public LogQuery toCommand() {
        int p = (page == null || page < 0) ? 0 : page;
        int s = (size == null || size <= 0) ? 10 : Math.min(500, size);
        return new LogQuery(trimToNull(guid), trimToNull(logger), p, s, limit);
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}