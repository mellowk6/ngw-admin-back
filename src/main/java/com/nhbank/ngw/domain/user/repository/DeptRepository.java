package com.nhbank.ngw.domain.user.repository;

import com.nhbank.ngw.domain.user.model.Dept;

import java.util.List;

public interface DeptRepository {

    List<Dept> findAll();
}