package com.nhbank.ngw.domain.user.service;

import com.nhbank.ngw.domain.shared.model.Page;
import com.nhbank.ngw.domain.user.command.RoleQuery;
import com.nhbank.ngw.domain.user.model.Role;

import java.util.List;

public interface RoleService {

    // 컨트롤러(GET)에서 사용하는 주 메서드
    Page<Role> search(String roleName, String menuLike, int page, int size);

    // 기존 RoleQuery 사용처를 위한 편의 메서드(원하면 삭제해도 됨)
    default Page<Role> list(RoleQuery q) {
        if (q == null) return search(null, null, 0, 10);
        return search(q.roleName(), q.menuScope(), q.page(), q.size());
    }

    void upsert(Role role);
    void deleteAll(List<String> roleNames);
}