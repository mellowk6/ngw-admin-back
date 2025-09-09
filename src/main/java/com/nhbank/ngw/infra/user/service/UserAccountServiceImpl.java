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
        if (!isUsernameAvailable(req.username())) {
            throw new DuplicateUsernameException(req.username());
        }
        UserAccount u = UserAccount.builder()
                .username(req.username())
                .passwordHash(passwordEncoder.encode(req.password()))
                .displayName(req.displayName())
                .deptCode(req.department())
                .company(req.company())
                .roles("ROLE_USER")
                .build();

        UserAccount saved = userAccountRepository.save(u);  // 저장 후 ID 세팅된 객체 반환
        return saved.getId();                                  // record 접근자
    }
}
