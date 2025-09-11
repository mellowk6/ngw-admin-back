package com.nhbank.ngw.api.user.dto.out;

import com.nhbank.ngw.domain.user.model.Role;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record RoleDto(
        String roleName,
        String menuScope,
        String createdAt,
        String updatedAt
) {
    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static RoleDto from(Role r) {
        LocalDateTime c = r.getCreatedAt();
        LocalDateTime u = r.getUpdatedAt();
        return new RoleDto(
                r.getRoleName(),
                r.getMenuScope(),
                (c == null ? null : c.format(DAY)),
                (u == null ? null : u.format(DAY))
        );
    }
}
