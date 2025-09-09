package com.nhbank.ngw.api.user.dto.out;

import com.nhbank.ngw.domain.user.model.Dept;
import lombok.Builder;

@Builder
public record DeptDto(String code, String name) {

    public static DeptDto from(Dept dept) {
        return DeptDto.builder()
                .code(dept.code())
                .name(dept.name())
                .build();
    }
}