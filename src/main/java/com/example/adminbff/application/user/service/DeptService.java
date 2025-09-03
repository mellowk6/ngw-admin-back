package com.example.adminbff.application.user.service;

import com.example.adminbff.api.user.dto.DeptDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptService {
    public List<DeptDto> list() {
        return List.of(
                new DeptDto("A001", "IT계정계전환추진팀"),
                new DeptDto("A002", "디지털플랫폼전환팀"),
                new DeptDto("A003", "디지털코어팀")
        );
    }
}