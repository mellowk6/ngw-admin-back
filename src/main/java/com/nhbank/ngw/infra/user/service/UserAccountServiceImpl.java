package com.nhbank.ngw.infra.user.service;

import com.nhbank.ngw.common.exception.DuplicateIdException;
import com.nhbank.ngw.domain.user.command.Signup;
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
    public boolean isIdAvailable(String id) {
        return !userAccountRepository.existsById(id);
    }

    @Override
    @Transactional
    public Long signup(Signup signup) {
        if (!isIdAvailable(signup.id())) {
            throw new DuplicateIdException(signup.id());
        }

        String encodedPassword = passwordEncoder.encode(signup.password());
        UserAccount userAccount = signup.toEntity(encodedPassword, "ROLE_USER");
        UserAccount savedUserAccount = userAccountRepository.save(userAccount);

        return savedUserAccount.getNo();
    }
}
