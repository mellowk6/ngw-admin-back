package com.nhbank.ngw.domain.user.command;

/** 사용자 조회 조건 (USERS 컬럼명 기준) */
public record UserQuery(
        String id,    // ID
        String name,  // NAME
        String roles,     // ROLES
        String deptCode,  // DEPT_CODE
        String company    // COMPANY
) { }
