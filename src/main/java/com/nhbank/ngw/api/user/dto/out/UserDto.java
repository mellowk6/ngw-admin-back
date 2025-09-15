package com.nhbank.ngw.api.user.dto.out;

import com.nhbank.ngw.domain.user.model.UserAccount;

/**
 * 목록/상세 공용 사용자 DTO (프론트 UserRow와 1:1 매핑)
 */
public record UserDto(
        String id,
        String name,
        String deptCode,
        String company,
        String roles,
        String createdAt,   // yyyy-MM-dd
        String updatedAt   // yyyy-MM-dd
) {
    public static UserDto from(UserAccount u) {
        return new UserDto(
                u.getId(),
                u.getName(),
                u.getDeptCode(),
                u.getCompany(),
                u.getRoles(),
                u.getCreatedAt() != null ? u.getCreatedAt().toString() : "",
                u.getUpdatedAt() != null ? u.getUpdatedAt().toString() : ""
        );
    }
}
