package com.nhbank.ngw.infra.user.repository;

import com.nhbank.ngw.domain.shared.model.Page;
import com.nhbank.ngw.domain.user.model.Role;
import com.nhbank.ngw.domain.user.repository.RoleRepository;
import com.nhbank.ngw.infra.user.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleMapper roleMapper;

    @Override
    public Page<Role> search(String roleName, String menuLike, int page, int size) {
        int p = Math.max(0, page);
        int s = Math.max(1, Math.min(500, size));
        int offset = p * s;

        long total = roleMapper.count(roleName, menuLike);
        List<Role> content = (total == 0)
                ? List.of()
                : roleMapper.selectPage(roleName, menuLike, offset, s);

        return Page.of(content, p, s, total);
    }

    @Transactional
    @Override
    public void upsert(Role r) {
        int updated = roleMapper.updateById(r);
        if (updated == 0) {
            roleMapper.insert(r);
        }
    }

    @Transactional
    @Override
    public void deleteAll(List<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) return;
        roleMapper.deleteAll(roleNames);
    }
}
