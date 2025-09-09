package com.nhbank.ngw.domain.user.service;

import com.nhbank.ngw.domain.user.command.Signup;

public interface UserAccountService {
    boolean isUsernameAvailable(String username);
    Long signup(Signup signup);   // 생성된 사용자 ID 반환
}
