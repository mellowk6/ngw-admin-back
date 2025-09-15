package com.nhbank.ngw.infra.user.service;

import com.nhbank.ngw.common.exception.DuplicateIdException;
import com.nhbank.ngw.domain.shared.model.Page;
import com.nhbank.ngw.domain.user.command.Signup;
import com.nhbank.ngw.domain.user.command.UpdateUserCommand;
import com.nhbank.ngw.domain.user.command.UserQuery;
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
        // 신규가입 시 기본 권한은 DEVELOPER
        UserAccount userAccount = signup.toEntity(encodedPassword, "DEVELOPER");
        UserAccount saved = userAccountRepository.save(userAccount);
        return saved.getNo();
    }

    /** 사용자 목록 조회 (공용 Page<T> 사용) */
    @Override
    @Transactional(readOnly = true)
    public Page<UserAccount> findUsers(UserQuery query, int page, int size) {
        // ✅ 레포지토리가 Page를 만들어 주므로 그대로 위임
        return userAccountRepository.findUsers(query, page, size);
    }

    /** 사용자 단건 수정 */
    @Override
    @Transactional
    public void updateUser(UpdateUserCommand cmd) {
        // 1) 기존 엔티티 조회(로그인 id 기준)
        UserAccount user = userAccountRepository.findByLoginId(cmd.id())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. id=" + cmd.id()));

        // 2) 변경 반영 (null 값은 무시)
        if (cmd.name() != null) user.setName(cmd.name());
        if (cmd.deptCode() != null) user.setDeptCode(cmd.deptCode());
        if (cmd.company() != null) user.setCompany(cmd.company());
        if (cmd.roles() != null) user.setRoles(cmd.roles());
        if (cmd.createdAt() != null) user.setCreatedAt(cmd.createdAt().atStartOfDay());
        if (cmd.updatedAt() != null) user.setUpdatedAt(cmd.updatedAt().atStartOfDay());

        // 3) 저장(INSERT/UPDATE를 save 한 곳에서 처리)
        userAccountRepository.save(user);
    }
}
