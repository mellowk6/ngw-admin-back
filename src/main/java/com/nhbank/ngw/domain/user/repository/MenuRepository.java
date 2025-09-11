package com.nhbank.ngw.domain.user.repository;

import com.nhbank.ngw.domain.user.model.Menu;

import java.util.List;

public interface MenuRepository {
    List<Menu> findAll();           // 드롭다운 용 전체
}