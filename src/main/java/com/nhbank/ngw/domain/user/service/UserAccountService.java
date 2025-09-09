package com.nhbank.ngw.domain.user.service;

import com.nhbank.ngw.api.user.dto.in.SignupRequest;
import com.nhbank.ngw.domain.user.model.Dept;

import java.util.List;

public interface UserAccountService {
    boolean isUsernameAvailable(String username);
    Long signup(SignupRequest req);   // 생성된 사용자 ID 반환
}
