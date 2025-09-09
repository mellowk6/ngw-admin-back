package com.nhbank.ngw.api.user.dto.in;

import com.nhbank.ngw.domain.user.command.Signup;
import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank String username,     // 사번
        @NotBlank String password,
        @NotBlank String displayName,
        @NotBlank String department,   // deptCode
        @NotBlank String company
) {
    public Signup toDomain(SignupRequest r) {
        return new Signup(
                r.username(),
                r.password(),
                r.displayName(),
                r.department(),
                r.company()
        );
    }
}