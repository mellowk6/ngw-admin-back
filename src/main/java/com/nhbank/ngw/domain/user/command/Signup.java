package com.nhbank.ngw.domain.user.command;

import com.nhbank.ngw.domain.user.model.UserAccount;

public record Signup(
        String id,          // 로그인 아이디
        String password,    // raw password
        String name,
        String deptCode,
        String company
) {
    /** 서비스에서 인코딩된 비밀번호와 역할을 주입해 엔티티 생성 */
    public UserAccount toEntity(String encodedPassword, String roles) {
        return UserAccount.builder()
                .id(id)
                .password(encodedPassword)  // 이미 해시된 값 주입
                .name(name)
                .deptCode(deptCode)
                .company(company)
                .roles(roles)
                .build();
    }
}
