package com.nhbank.ngw.domain.user.command;

import com.nhbank.ngw.domain.user.model.UserAccount;

public record Signup(
        String username,
        String rawPassword,
        String displayName,
        String department,   // deptCode
        String company
) {
    public UserAccount toEntity(String encodedPassword, String roles) {
        return UserAccount.builder()
                .username(username)
                .passwordHash(encodedPassword) // 이미 해시된 값 주입
                .displayName(displayName)
                .deptCode(department)
                .company(company)
                .roles(roles)
                .build();
    }
}