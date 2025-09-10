package com.nhbank.ngw.domain.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password")   // 비밀번호 로그 노출 방지
public class UserAccount {
    /** PK (users.no) */
    private Long no;

    /** 로그인 아이디 (users.id) */
    private String id;

    /** 해시 비밀번호 (users.password) */
    @JsonIgnore
    private String password;

    /** 사용자명 (users.name) */
    private String name;

    private String deptCode;       // users.dept_code
    private String company;        // users.company
    private String roles;          // "ROLE_USER,ROLE_ADMIN"
    private LocalDateTime createdAt;   // users.created_at
    private LocalDateTime updatedAt;   // users.updated_at

    public List<String> roleList() {
        return (roles == null || roles.isBlank())
                ? List.of()
                : Arrays.stream(roles.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
