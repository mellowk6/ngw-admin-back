package com.nhbank.ngw.domain.user.repository;

import com.nhbank.ngw.domain.user.model.UserAccount;

import java.util.Optional;

public interface UserAccountRepository {

    /** 로그인 아이디(id) 존재 여부 */
    boolean existsById(String id);

    /** 로그인 아이디(id)로 조회 */
    Optional<UserAccount> findByLoginId(String id);

    /** 신규 저장 또는 갱신 (PK: no) */
    UserAccount save(UserAccount user);
}
