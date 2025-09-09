package com.nhbank.ngw.domain.user.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserAccount {
    private Long id;
    private String username;
    private String passwordHash;
    private String displayName;
    private String deptCode;
    private String company;
    private String roles;            // "ROLE_USER,ROLE_ADMIN"
    private LocalDateTime createdAt;

    public List<String> roleList() {
        return roles == null || roles.isBlank()
                ? List.of()
                : Arrays.stream(roles.split(",")).map(String::trim).toList();
    }
}