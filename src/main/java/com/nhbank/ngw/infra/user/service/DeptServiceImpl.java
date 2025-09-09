package com.nhbank.ngw.infra.user.service;

import com.nhbank.ngw.domain.user.repository.DeptRepository;
import com.nhbank.ngw.domain.user.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.nhbank.ngw.domain.user.model.Dept;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeptServiceImpl implements DeptService {

    private final DeptRepository deptRepository;

    public List<Dept> list() {
        return deptRepository.findAll().stream().toList();
    }
}