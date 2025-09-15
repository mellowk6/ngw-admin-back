package com.nhbank.ngw.domain.user.command;

import java.time.LocalDate;

/** 단건 사용자 수정 커맨드 (USERS 컬럼명 기준) */
public record UpdateUserCommand(
        String id,      // ID (PK)
        String name,    // NAME
        String deptCode,    // DEPT_CODE
        String company,     // COMPANY
        String roles,       // ROLES
        LocalDate createdAt, // CREATED_AT  (필요 시 LocalDateTime로 변경 가능)
        LocalDate updatedAt  // UPDATED_AT
) { }
