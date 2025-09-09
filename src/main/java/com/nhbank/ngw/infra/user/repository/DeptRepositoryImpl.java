package com.nhbank.ngw.infra.user.repository;

import com.nhbank.ngw.infra.user.mapper.DeptMapper;
import com.nhbank.ngw.domain.user.model.Dept;
import com.nhbank.ngw.domain.user.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeptRepositoryImpl implements DeptRepository {
    private final DeptMapper deptMapper;

    public List<Dept> findAll() {
        return deptMapper.selectAll();
    }
}