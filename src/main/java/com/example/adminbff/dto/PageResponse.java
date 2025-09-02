package com.example.adminbff.dto;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;

public record PageResponse<T>(
        List<T> content,
        int number,
        int size,
        long totalElements,
        int totalPages
) {
    // DB Page -> 계약 DTO 변환 편의 메서드
    public static <E, D> PageResponse<D> from(Page<E> p, Function<E, D> mapper) {
        return new PageResponse<>(
                p.getContent().stream().map(mapper).toList(),
                p.getNumber(),
                p.getSize(),
                p.getTotalElements(),
                p.getTotalPages()
        );
    }
}