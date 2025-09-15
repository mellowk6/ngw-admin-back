package com.nhbank.ngw.domain.user.repository;

import com.nhbank.ngw.domain.shared.model.Page;   // ✅ 공용 Page
import com.nhbank.ngw.domain.user.command.UserQuery;
import com.nhbank.ngw.domain.user.model.UserAccount;

import java.util.Optional;

public interface UserAccountRepository {

    boolean existsById(String id);

    Optional<UserAccount> findByLoginId(String id);

    UserAccount save(UserAccount user);

    /** ✅ 공용 Page로 통일 */
    Page<UserAccount> findUsers(UserQuery query, int page, int size);
}
