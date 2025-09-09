package com.nhbank.ngw.common.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * 표준 성공 응답 래퍼.
 * 프런트에는 아래 형태로 전달됩니다.
 * {
 *   "timestamp": "2025-09-03T08:31:12.345Z",
 *   "path": "/api/...",
 *   "data": { ... 실제 응답 페이로드 ... }
 * }
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private OffsetDateTime timestamp;
    private String path;
    private T data;

    public ApiResponse() { }

    public ApiResponse(OffsetDateTime timestamp, String path, T data) {
        this.timestamp = timestamp;
        this.path = path;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(OffsetDateTime.now(), null, data); // path는 나중에 Advice에서 채움
    }

    /** 편의 팩토리 */
    public static <T> ApiResponse<T> of(String path, T data) {
        return new ApiResponse<>(OffsetDateTime.now(), path, data);
    }

}