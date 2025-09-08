package com.nhbank.ngw.infra.user.repository;

import com.nhbank.ngw.infra.user.mapper.UserMapper;
import com.nhbank.ngw.domain.user.model.UserAccount;
import com.nhbank.ngw.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper mapper;

    public boolean existsByUsername(String username) {
        return mapper.existsByUsername(username);
    }

    public void save(UserAccount user) {
        mapper.insert(user); // int 반환이면 무시해도 됨
    }

    public Optional<UserAccount> findByUsername(String username) {
        // ⬅️ 매퍼가 못 찾으면 null을 반환하므로 여기서 Optional 감싸기
        return Optional.ofNullable(mapper.findByUsername(username));
    }
}