package com.nhbank.ngw.infra.user.mapper;

import com.nhbank.ngw.domain.user.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    long count(@Param("roleName") String roleName,
               @Param("menuLike") String menuLike);

    List<Role> selectPage(@Param("roleName") String roleName,
                          @Param("menuLike") String menuLike,
                          @Param("offset") int offset,
                          @Param("size") int size);

    int updateById(Role role);
    int insert(Role role);
    void deleteAll(@Param("roleNames") List<String> roleNames);
}
