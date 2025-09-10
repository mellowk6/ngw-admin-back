package com.nhbank.ngw.api.user.dto.in;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String id,
        @NotBlank String password
) {}
