package com.nhbank.ngw.infra.user.service;

import com.nhbank.ngw.api.user.dto.in.SignupRequest;
import com.nhbank.ngw.common.exception.DuplicateUsernameException;
import com.nhbank.ngw.domain.user.model.UserAccount;
import com.nhbank.ngw.domain.user.repository.UserAccountRepository;
import com.nhbank.ngw.domain.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public boolean isUsernameAvailable(String username) {
        return !userAccountRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public Long signup(SignupRequest req) {
        // 중복 검사
        if (userAccountRepository.existsByUsername(req.username())) {
            throw new DuplicateUsernameException(req.username());
        }
        // 비밀번호 해시
        String encoded = passwordEncoder.encode(req.password());

        // 엔티티 생성 및 저장 (record 사용 시 빌더/팩토리 일관화)
        UserAccount u = UserAccount.builder()
                .username(req.username())
                .passwordHash(encoded)
                .displayName(req.displayName())
                .deptCode(req.department())
                .company(req.company())
                .roles("ROLE_USER")
                .build();

        UserAccount saved = userAccountRepository.save(u);
        // record면 saved.id(), 클래시면 saved.getId()
        return extractId(saved);
    }

    // record/클래스 양쪽 대응(필요시 한쪽만 쓰도록 정리)
    private Long extractId(UserAccount u) {
        try {
            return (Long) u.getClass().getMethod("getId").invoke(u);
        } catch (Exception e) {
            try {
                return (Long) u.getClass().getMethod("id").invoke(u);
            } catch (Exception ex) {
                throw new IllegalStateException("UserAccount에 id 접근자가 없습니다.");
            }
        }
    }
}
