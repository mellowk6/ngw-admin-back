package com.nhbank.ngw.infra.user.mapper;

import com.nhbank.ngw.domain.user.model.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {
    List<Menu> selectAll();
}