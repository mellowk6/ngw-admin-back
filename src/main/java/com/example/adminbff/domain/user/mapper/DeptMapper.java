package com.example.adminbff.domain.user.mapper;

import com.example.adminbff.domain.user.model.Dept;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface DeptMapper {
    List<Dept> selectAll();
}