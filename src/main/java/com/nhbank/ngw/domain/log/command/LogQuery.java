package com.nhbank.ngw.domain.log.command;


public record LogQuery(
        String guid,          // nullable
        String logger,        // nullable (logger name prefix 등)
        int page,             // 0-base
        int size,             // 페이지 크기
        Integer limit         // guid 중심 조회 시 상한 (page/size와 배타적으로 사용)
) {
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 50;
    public static final int MAX_SIZE     = 500;
}