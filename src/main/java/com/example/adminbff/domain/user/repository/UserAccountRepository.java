package com.example.adminbff.domain.user.repository;

import com.example.adminbff.domain.user.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    boolean existsByUsername(String username);
    Optional<UserAccount> findByUsername(String username);
}