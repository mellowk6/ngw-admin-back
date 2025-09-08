package com.nhbank.ngw.infra.user.repository;

import com.nhbank.ngw.infra.user.mapper.UserMapper;
import com.nhbank.ngw.domain.user.model.UserAccount;
import com.nhbank.ngw.domain.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserAccountRepositoryImpl implements UserAccountRepository {

    private final UserMapper mapper;

    public boolean existsByUsername(String username) {
        return mapper.existsByUsername(username);
    }

    public Optional<UserAccount> findByUsername(String username) {
        return Optional.ofNullable(mapper.findByUsername(username));
    }

    /** JPA의 save 시그니처를 흉내내 동일하게 제공 */
    public UserAccount save(UserAccount user) {
        if (user.getId() == null) {
            mapper.insert(user); // id 세팅됨
        } else {
            int updated = mapper.update(user);
            if (updated == 0) {
                throw new IllegalStateException("수정 대상이 없습니다. id=" + user.getId());
            }
        }
        return user;
    }
}