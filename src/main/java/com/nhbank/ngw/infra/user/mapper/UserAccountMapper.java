package com.nhbank.ngw.infra.user.mapper;

import com.nhbank.ngw.domain.user.model.UserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAccountMapper {

    /** 로그인 아이디(id) 존재 여부 */
    boolean existsById(@Param("id") String id);

    /** 로그인 아이디(id)로 조회 */
    UserAccount findByLoginId(@Param("id") String id);

    /** 신규 저장 — useGeneratedKeys 로 PK(no) 채움 (XML에서 keyProperty="no", keyColumn="no") */
    int insert(UserAccount user);

    /** 수정 — PK(no) 기준 업데이트 */
    int updateByNo(UserAccount user);
}
