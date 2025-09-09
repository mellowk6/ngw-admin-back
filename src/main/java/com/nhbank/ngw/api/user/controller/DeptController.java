package com.nhbank.ngw.api.user.controller;

import com.nhbank.ngw.api.user.dto.out.DeptDto;
import com.nhbank.ngw.domain.user.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/dept")
public class DeptController {

    private final DeptService deptService;

    /**
     * 부서 목록
     */
    @GetMapping("/list")
    public List<DeptDto> list() {
        return deptService.list().stream()
                .map(DeptDto::from)   // 변환 메서드 호출
                .toList();
    }

}
