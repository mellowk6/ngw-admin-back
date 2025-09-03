package com.example.adminbff.api.user.dto;

import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank String username,     // 사번
        @NotBlank String password,
        @NotBlank String displayName,
        @NotBlank String department,   // deptCode
        @NotBlank String company
) {}