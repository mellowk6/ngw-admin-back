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

    /** 로그인 아이디 사용 가능 여부 */
    @GetMapping("/check-id")
    public CheckIdResponse checkId(@RequestParam String id) {
        return new CheckIdResponse(userAccountService.isIdAvailable(id));
    }

    /** 회원 가입: 서비스는 PK(no) 반환 */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest signupRequest) {
        Long no = userAccountService.signup(signupRequest.toCommand());
        return ResponseEntity.created(URI.create("/api/user/" + no)).build();
    }

    /** 내 정보 요약: 인증 주체명은 로그인 아이디(id) */
    @GetMapping("/my-info")
    public Map<String, Object> myInfo(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return Map.of("authenticated", false);
        }
        return Map.of(
                "authenticated", true,
                "id", auth.getName(),
                "roles", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );
    }
}
