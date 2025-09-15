package com.nhbank.ngw.api.user.dto.in;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 단건 수정 요청 바디 (프론트 users.ts의 payload와 동일)
 */
public record UserUpdateRequest(
        String name,
        String deptCode,       // 코드로 전달
        String company,
        String roles,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAt,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updatedAt
) { }
