package com.nhbank.ngw.api.user.controller;

import com.nhbank.ngw.api.log.dto.out.PageResponse; // 기존 PageResponse 재사용
import com.nhbank.ngw.api.shared.dto.ApiResponse;
import com.nhbank.ngw.api.shared.mapper.PageMapper;
import com.nhbank.ngw.api.user.dto.in.RoleDeleteRequest;
import com.nhbank.ngw.api.user.dto.in.RoleUpsertRequest;
import com.nhbank.ngw.api.user.dto.out.RoleDto;
import com.nhbank.ngw.domain.user.model.Role;
import com.nhbank.ngw.domain.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
@Slf4j
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ApiResponse<PageResponse<RoleDto>> list(
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) String menuLike,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        int p = (page == null || page < 0) ? 0 : page;
        int s = (size == null || size <= 0) ? 10 : Math.min(500, size);
        var result = roleService.search(roleName, menuLike, p, s);
        var dto    = PageMapper.toDto(result, RoleDto::from);
        return ApiResponse.ok(dto);
    }

    /** 업서트: PUT /api/roles  (프론트도 경로변수 없이 base로 PUT) */
    @PutMapping
    public ApiResponse<Boolean> upsert(@RequestBody RoleUpsertRequest body) {
        log.info("UPSERT roleName={}, menuScope={}", body.roleName(), body.menuScope());
        roleService.upsert(Role.builder()
                .roleName(body.roleName())
                .menuScope(body.menuScope())
                .build());
        return ApiResponse.ok(true);
    }

    /** 삭제: DELETE */
    @DeleteMapping
    public ApiResponse<Boolean> delete(@RequestBody RoleDeleteRequest body) {
        log.info("DELETE roles: {}", body.roleNames());
        roleService.deleteAll(body.roleNames());
        return ApiResponse.ok(true);
    }
}
