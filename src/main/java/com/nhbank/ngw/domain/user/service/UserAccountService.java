package com.nhbank.ngw.domain.user.service;

import com.nhbank.ngw.domain.shared.model.Page;
import com.nhbank.ngw.domain.user.command.Signup;
import com.nhbank.ngw.domain.user.command.UpdateUserCommand;
import com.nhbank.ngw.domain.user.command.UserQuery;
import com.nhbank.ngw.domain.user.model.UserAccount;

public interface UserAccountService {
    /** 로그인 아이디(id) 사용 가능 여부 */
    boolean isIdAvailable(String id);

    /** 회원 가입 → 생성된 사용자 PK(no) 반환 */
    Long signup(Signup signup);

    /** 사용자 페이지 조회(공통 Page<T> 사용) */
    Page<UserAccount> findUsers(UserQuery query, int page, int size);

    /** 사용자 단건 수정 */
    void updateUser(UpdateUserCommand cmd);
}
