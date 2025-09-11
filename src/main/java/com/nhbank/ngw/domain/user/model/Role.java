package com.nhbank.ngw.domain.user.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Role {
    private String roleName;
    private String menuScope;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}