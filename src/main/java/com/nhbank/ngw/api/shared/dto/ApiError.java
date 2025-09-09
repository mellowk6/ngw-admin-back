package com.nhbank.ngw.api.shared.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiError(
        OffsetDateTime timestamp,
        String path,
        int status,
        String error,      // "Bad Request", "Not Found" ...
        String message,    // 사람이 읽을 메시지
        String code,       // 선택: 비즈니스 에러 코드(예: USER_NOT_FOUND)
        String traceId,    // 선택: MDC 등 상관관계 ID
        List<FieldError> details // 선택: 필드 검증 에러 목록
) {
    public record FieldError(String field, String message) {}
}