package com.nhbank.ngw.domain.user.entity;

import lombok.*;

@Builder
public record Dept(
        String code,
        String name
) {
}
