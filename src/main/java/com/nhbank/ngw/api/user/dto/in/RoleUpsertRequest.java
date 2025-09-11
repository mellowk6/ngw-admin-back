package com.nhbank.ngw.api.user.dto.in;

import lombok.Builder;

@Builder
public record RoleUpsertRequest(
        String roleName,
        String menuScope
) {}
