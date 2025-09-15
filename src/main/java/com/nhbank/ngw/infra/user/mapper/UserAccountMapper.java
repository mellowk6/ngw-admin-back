package com.nhbank.ngw.infra.user.mapper;

import com.nhbank.ngw.domain.user.model.UserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserAccountMapper {
    boolean existsById(@Param("id") String id);
    UserAccount findByLoginId(@Param("id") String id);
    int insert(UserAccount user);
    int updateByNo(UserAccount user);

    // ✅ 목록/카운트
    long countByQuery(@Param("id") String id,
                      @Param("name") String name,
                      @Param("roles") String roles,
                      @Param("deptCode") String deptCode,
                      @Param("company") String company);

    List<UserAccount> findPageByQuery(@Param("id") String id,
                                      @Param("name") String name,
                                      @Param("roles") String roles,
                                      @Param("deptCode") String deptCode,
                                      @Param("company") String company,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);
}
