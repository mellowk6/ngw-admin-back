package com.nhbank.ngw.infra.user.service;

import com.nhbank.ngw.common.exception.DuplicateUsernameException;
import com.nhbank.ngw.domain.user.command.Signup;
import com.nhbank.ngw.domain.user.entity.UserAccount;
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
    public Long signup(Signup signup) {
        if (!isUsernameAvailable(signup.username())) {
            throw new DuplicateUsernameException(signup.username());
        }

        UserAccount userAccount = signup.toEntity("ROLE_USER", passwordEncoder);
        UserAccount savedUserAccount = userAccountRepository.save(userAccount);

        return savedUserAccount.getId();
    }
}
