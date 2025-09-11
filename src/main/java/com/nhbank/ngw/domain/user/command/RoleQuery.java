package com.nhbank.ngw.domain.user.command;

public record RoleQuery(
        String roleName,   // like 검색(부분일치)
        String menuScope,  // 정확히 일치 (ALL 선택 시 null로 보냄)
        int page,          // 0-base
        int size           // 페이지 크기
) {
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 10;
    public static final int MAX_SIZE     = 100;
}
