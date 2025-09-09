package com.nhbank.ngw.infra.user.repository;

import com.nhbank.ngw.infra.user.mapper.UserAccountMapper;
import com.nhbank.ngw.domain.user.entity.UserAccount;
import com.nhbank.ngw.domain.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserAccountRepositoryImpl implements UserAccountRepository {

    private final UserAccountMapper userAccountMapper;

    public boolean existsByUsername(String username) {
        return userAccountMapper.existsByUsername(username);
    }

    public Optional<UserAccount> findByUsername(String username) {
        return Optional.ofNullable(userAccountMapper.findByUsername(username));
    }

    /** JPA의 save 시그니처를 흉내내 동일하게 제공 */
    public UserAccount save(UserAccount user) {
        if (user.getId() == null) {
            userAccountMapper.insert(user); // id 세팅됨
        } else {
            int updated = userAccountMapper.update(user);
            if (updated == 0) {
                throw new IllegalStateException("수정 대상이 없습니다. id=" + user.getId());
            }
        }
        return user;
    }
}