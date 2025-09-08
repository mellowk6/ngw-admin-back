package com.nhbank.ngw.api.log.dto.in;

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
}