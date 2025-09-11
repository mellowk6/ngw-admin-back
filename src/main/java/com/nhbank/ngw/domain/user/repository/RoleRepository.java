package com.nhbank.ngw.domain.user.repository;

import com.nhbank.ngw.domain.shared.model.Page;
import com.nhbank.ngw.domain.user.model.Role;

import java.util.List;

public interface RoleRepository {
    Page<Role> search(String roleName, String menuLike, int page, int size);
    void upsert(Role role);
    void deleteAll(List<String> roleNames);
}