package com.nhbank.ngw.domain.user.repository;

import com.nhbank.ngw.domain.user.model.UserAccount;

import java.util.Optional;

public interface UserRepository {


    boolean existsByUsername(String username);

    void save(UserAccount user);

    Optional<UserAccount> findByUsername(String username);
}