package com.nhbank.ngw.infra.user.repository;

import com.nhbank.ngw.domain.shared.model.Page;
import com.nhbank.ngw.domain.user.command.UserQuery;
import com.nhbank.ngw.domain.user.model.UserAccount;
import com.nhbank.ngw.domain.user.repository.UserAccountRepository;
import com.nhbank.ngw.infra.user.mapper.UserAccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserAccountRepositoryImpl implements UserAccountRepository {

    private final UserAccountMapper userAccountMapper;

    @Override
    public boolean existsById(String id) {
        return userAccountMapper.existsById(id);
    }

    @Override
    public Optional<UserAccount> findByLoginId(String id) {
        return Optional.ofNullable(userAccountMapper.findByLoginId(id));
    }

    @Override
    public UserAccount save(UserAccount user) {
        if (user.getNo() == null) {
            userAccountMapper.insert(user); // useGeneratedKeys 로 PK(no) 세팅
        } else {
            int updated = userAccountMapper.updateByNo(user);
            if (updated == 0) {
                throw new IllegalStateException("수정 대상이 없습니다. no=" + user.getNo());
            }
        }
        return user;
    }

    @Override
    public Page<UserAccount> findUsers(UserQuery q, int page, int size) {
        int p = Math.max(0, page);
        int s = Math.max(1, size);
        int offset = p * s;

        long total = userAccountMapper.countByQuery(
                q.id(), q.name(), q.roles(), q.deptCode(), q.company()
        );

        List<UserAccount> content = (total > 0)
                ? userAccountMapper.findPageByQuery(
                q.id(), q.name(), q.roles(), q.deptCode(), q.company(),
                offset, s
        )
                : Collections.emptyList();

        return Page.of(content, p, s, total);
    }
}
