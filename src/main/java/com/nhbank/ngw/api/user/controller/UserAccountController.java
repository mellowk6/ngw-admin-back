package com.nhbank.ngw.api.user.controller;

import com.nhbank.ngw.api.user.dto.in.SignupRequest;
import com.nhbank.ngw.api.user.dto.out.CheckIdResponse;

import com.nhbank.ngw.domain.user.service.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @GetMapping("/check-id")
    public CheckIdResponse checkId(@RequestParam String userId) {
        return new CheckIdResponse(userAccountService.isUsernameAvailable(userId));
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest signupRequest) {
        Long id = userAccountService.signup(signupRequest.toDomain(signupRequest));
        // 컨트롤러 베이스 경로가 /api/user 이므로 /api/user/{id}로 맞춤
        return ResponseEntity.created(URI.create("/api/user/" + id)).build();
    }

    @GetMapping("/my-info")
    public Map<String, Object> myInfo(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return Map.of("authenticated", false);
        }
        return Map.of(
                "authenticated", true,
                "username", auth.getName(),
                "roles", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );
    }
}
