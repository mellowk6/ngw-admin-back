package com.nhbank.ngw.api.user.dto.in;

/**
 * 사용자 조회 파라미터 (선택값, 페이지네이션 포함)
 * 컨트롤러에서 @ModelAttribute 또는 수동 매핑으로 사용 가능.
 */
public record UserQueryRequest(
        Integer page,          // 0-base
        Integer size,          // 페이지 크기
        String id,
        String name,
        String roles,
        String deptCode,       // 코드로 검색
        String company
) { }
