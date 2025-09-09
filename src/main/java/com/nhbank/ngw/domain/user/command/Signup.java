package com.nhbank.ngw.domain.user.command;

import com.nhbank.ngw.domain.user.entity.UserAccount;
import org.springframework.security.crypto.password.PasswordEncoder;

public record Signup(
        String username,
        String rawPassword,
        String displayName,
        String department,   // deptCode
        String company
) {
    public UserAccount toEntity(String roles, PasswordEncoder passwordEncoder) {
        return UserAccount.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .displayName(displayName)
                .deptCode(department)
                .company(company)
                .roles(roles)
                .build();
    }
}