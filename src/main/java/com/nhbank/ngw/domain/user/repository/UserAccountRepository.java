package com.nhbank.ngw.domain.user.repository;

import com.nhbank.ngw.domain.user.entity.UserAccount;

import java.util.Optional;

public interface UserAccountRepository {


    boolean existsByUsername(String username);

    Optional<UserAccount> findByUsername(String username);

    /** JPA의 save 시그니처를 흉내내 동일하게 제공 */
    UserAccount save(UserAccount user);
}