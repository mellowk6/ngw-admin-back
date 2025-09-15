package com.nhbank.ngw.api.user.controller;

import com.nhbank.ngw.api.log.dto.out.PageResponse;     // ✅ 기존 공용 PageResponse 재사용
import com.nhbank.ngw.api.shared.dto.ApiResponse;
import com.nhbank.ngw.api.shared.mapper.PageMapper;      // ✅ domain Page → api PageResponse 변환기
import com.nhbank.ngw.api.user.dto.in.UserUpdateRequest; // body DTO (프론트 키 유지)
import com.nhbank.ngw.api.user.dto.out.UserDto;          // 목록 응답 DTO (프론트 키 유지)
import com.nhbank.ngw.domain.shared.model.Page;          // ✅ 공용 Page
import com.nhbank.ngw.domain.user.command.UpdateUserCommand;
import com.nhbank.ngw.domain.user.command.UserQuery;
import com.nhbank.ngw.domain.user.model.UserAccount;
import com.nhbank.ngw.domain.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserMngController {

    private final UserAccountService userAccountService;

    /** 목록 조회: GET /api/users */
    @GetMapping
    public ApiResponse<PageResponse<UserDto>> list(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            // 신/구 파라미터 호환
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "roles", required = false) String roles,
            @RequestParam(name = "deptCode", required = false) String deptCode,
            @RequestParam(name = "company", required = false) String company
    ) {
        int p = (page == null || page < 0) ? 0 : page;
        int s = (size == null || size <= 0) ? 10 : Math.min(500, size);

        // ✅ 공용 UserQuery 사용
        UserQuery q = new UserQuery(id, name, roles, deptCode, company);

        // ✅ 서비스는 공용 Page<UserAccount> 반환
        Page<UserAccount> result = userAccountService.findUsers(q, p, s);

        // ✅ 공용 Page → API PageResponse 매핑
        PageResponse<UserDto> dto = PageMapper.toDto(result, UserDto::from);

        return ApiResponse.ok(dto);
    }

    /** 단건 수정: PUT /api/users/{id} */
    @PutMapping("/{id}")
    public ApiResponse<Boolean> update(
            @PathVariable String id,
            @RequestBody UserUpdateRequest body
    ) {
        // 프론트 바디 키(userName, deptCode, companyName, role, joinedAt, updatedAt) → 도메인 커맨드로 변환
        UpdateUserCommand cmd = new UpdateUserCommand(
                id,
                body.name(),
                body.deptCode(),
                body.company(),
                body.roles(),
                body.createdAt(),
                body.updatedAt()
        );
        userAccountService.updateUser(cmd);
        return ApiResponse.ok(true);
    }
}
