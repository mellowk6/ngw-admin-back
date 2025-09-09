package com.nhbank.ngw.domain.user.model;

import lombok.*;

@Builder
public record Dept(
        String code,
        String name
) {
}
