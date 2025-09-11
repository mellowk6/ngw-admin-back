package com.nhbank.ngw.infra.user.service;

import com.nhbank.ngw.domain.shared.model.Page;
import com.nhbank.ngw.domain.user.command.RoleQuery;
import com.nhbank.ngw.domain.user.model.Role;
import com.nhbank.ngw.domain.user.repository.RoleRepository;
import com.nhbank.ngw.domain.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repo;

    @Override
    public Page<Role> search(String roleName, String menuLike, int page, int size) {
        return repo.search(roleName, menuLike, page, size);
    }

    @Override
    public Page<Role> list(RoleQuery q) {
        if (q == null) return repo.search(null, null, 0, 10);
        return repo.search(q.roleName(), q.menuScope(), q.page(), q.size()); // ← 여기!
    }

    @Override
    public void upsert(Role role) {
        repo.upsert(role);
    }

    @Override
    public void deleteAll(List<String> roleNames) {
        repo.deleteAll(roleNames);
    }
}
