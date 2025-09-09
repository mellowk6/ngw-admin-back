package com.nhbank.ngw.infra.user.mapper;

import com.nhbank.ngw.domain.user.entity.UserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAccountMapper {
    boolean existsByUsername(@Param("username") String username);

    UserAccount findByUsername(@Param("username") String username);

    /** 신규 저장 — useGeneratedKeys 로 id 채움 (XML) */
    int insert(UserAccount user);

    /** 수정 — id 기준 업데이트 */
    int update(UserAccount user);
}