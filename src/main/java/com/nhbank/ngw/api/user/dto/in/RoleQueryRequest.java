package com.nhbank.ngw.api.user.dto.in;

import com.nhbank.ngw.domain.user.command.RoleQuery;
import lombok.Builder;

@Builder
public record RoleQueryRequest(
        String roleName,
        String menuScope,    // "ALL" 이면 null 취급
        Integer page,
        Integer size
) {
    public RoleQuery toCommand() {
        int p = (page == null || page < 0) ? 0 : page;
        int s = (size == null || size <= 0) ? 10 : Math.min(100, size);
        return new RoleQuery(
                roleName == null || roleName.isBlank() ? null : roleName.trim(),
                (menuScope == null || menuScope.isBlank() || "ALL".equalsIgnoreCase(menuScope)) ? null : menuScope.trim(),
                p, s
        );
    }
}
