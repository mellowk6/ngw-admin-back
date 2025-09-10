package com.nhbank.ngw.api.user.dto.in;

import com.nhbank.ngw.domain.user.command.Signup;
import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank String id,        // 로그인 아이디
        @NotBlank String password,
        @NotBlank String name,
        @NotBlank String deptCode,
        @NotBlank String company
) {
    /** 컨트롤러에서 그대로 사용: signupRequest.toCommand() */
    public Signup toCommand() {
        return new Signup(id, password, name, deptCode, company);
    }
}
