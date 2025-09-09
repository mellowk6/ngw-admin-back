package com.nhbank.ngw.domain.user.service;

import com.nhbank.ngw.api.user.dto.in.SignupRequest;

public interface UserAccountService {
    boolean isUsernameAvailable(String username);
    Long signup(SignupRequest req);   // 생성된 사용자 ID 반환
}
