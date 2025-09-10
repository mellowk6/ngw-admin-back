package com.nhbank.ngw.infra.user.service;

import com.nhbank.ngw.domain.user.model.UserAccount;
import com.nhbank.ngw.domain.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    /**
     * Spring Security 표준 시그니처이므로 파라미터명은 username 이지만,
     * 실제로는 "로그인 아이디(id)" 로 사용한다.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount u = userAccountRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        var authorities = u.roleList().stream()
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .map(SimpleGrantedAuthority::new)
                .toList();

        return User.withUsername(u.getId())       // 로그인 아이디(id)
                .password(u.getPassword())        // BCrypt 인코딩된 해시
                .authorities(authorities)         // ROLE_ 접두어 보장
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
