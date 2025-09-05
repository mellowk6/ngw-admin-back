package com.example.adminbff.domain.user.repository;

import com.example.adminbff.domain.user.mapper.DeptMapper;
import com.example.adminbff.domain.user.model.Dept;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeptRepository {
    private final DeptMapper mapper;

    public List<Dept> findAll() {
        return mapper.selectAll();
    }
}