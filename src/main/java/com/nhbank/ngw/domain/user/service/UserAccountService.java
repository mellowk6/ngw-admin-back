package com.nhbank.ngw.domain.user.service;

import com.nhbank.ngw.domain.user.command.Signup;

public interface UserAccountService {
    /** 로그인 아이디(id) 사용 가능 여부 */
    boolean isIdAvailable(String id);

    /** 회원 가입 → 생성된 사용자 PK(no) 반환 */
    Long signup(Signup signup);
}
