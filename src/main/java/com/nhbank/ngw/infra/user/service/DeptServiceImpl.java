package com.nhbank.ngw.infra.user.service;

import com.nhbank.ngw.api.user.dto.out.DeptDto;
import com.nhbank.ngw.domain.user.repository.DeptRepository;
import com.nhbank.ngw.domain.user.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeptServiceImpl implements DeptService {

    private final DeptRepository repo;

    public List<DeptDto> list() {
        return repo.findAll().stream()
                .map(d -> new DeptDto(d.getCode(), d.getName()))
                .toList();
    }
}