package com.example.adminbff.application.user.service;

import com.example.adminbff.api.user.dto.DeptDto;
import com.example.adminbff.domain.user.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeptService {

    private final DeptRepository repo;

    public List<DeptDto> list() {
        return repo.findAll().stream()
                .map(d -> new DeptDto(d.getCode(), d.getName()))
                .toList();
    }
}