package com.example.adminbff.api.user.controller;


import com.example.adminbff.api.user.dto.CheckIdResponse;
import com.example.adminbff.api.user.dto.DeptDto;
import com.example.adminbff.api.user.dto.SignupRequest;
import com.example.adminbff.domain.user.model.UserAccount;
import com.example.adminbff.domain.user.repository.UserAccountRepository;
import com.example.adminbff.application.user.service.DeptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthPublicController {

    private final UserAccountRepository users;
    private final PasswordEncoder passwordEncoder;
    private final DeptService deptService;

    /** 사용자ID(사번) 중복 확인 */
    @GetMapping("/api/auth/check-id")
    public CheckIdResponse checkId(@RequestParam String userId) {
        boolean available = !users.existsByUsername(userId);
        return new CheckIdResponse(available);
    }

    /** 부서 목록 */
    @GetMapping("/api/dept/list")
    public List<DeptDto> listDepts() {
        return deptService.list();
    }

    /** 회원가입 */
    @PostMapping("/api/auth/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest req) {
        if (users.existsByUsername(req.username())) {
            return ResponseEntity.status(409).build(); // Conflict
        }
        UserAccount u = UserAccount.builder()
                .username(req.username())
                .passwordHash(passwordEncoder.encode(req.password()))
                .displayName(req.displayName())
                .deptCode(req.department())
                .company(req.company())
                .roles("ROLE_USER")
                .build();
        users.save(u);
        return ResponseEntity.created(URI.create("/api/users/" + u.getId())).build();
    }
}