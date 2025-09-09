package com.nhbank.ngw.api.log.dto.out;

import java.util.List;


public record PageResponse<T>(
        List<T> items,
        int page,
        int size,
        long total,
        int totalPages
) {
    public static <T> PageResponse<T> of(List<T> items, int page, int size, long total, int totalPages) {
        return new PageResponse<>(items, page, size, total, totalPages);
    }

    public static <T> PageResponse<T> empty(int page, int size) {
        return new PageResponse<>(List.of(), page, size, 0L, 0);
    }
}
