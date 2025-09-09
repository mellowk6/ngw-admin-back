package com.nhbank.ngw.domain.shared.model;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public record Page<T>(List<T> content, int page, int size, long total) {

    public static <T> Page<T> of(List<T> content, int page, int size, long total) {
        return new Page<>(content == null ? List.of() : List.copyOf(content),
                Math.max(0, page),
                Math.max(0, size),
                Math.max(0, total));
    }

    public static <T> Page<T> empty(int page, int size) {
        return new Page<>(Collections.emptyList(),
                Math.max(0, page),
                Math.max(0, size),
                0L);
    }

    /** DTO 매핑 등에 유용한 content 변환 */
    public <R> Page<R> map(Function<? super T, R> mapper) {
        List<R> mapped = this.content.stream().map(mapper).toList();
        return new Page<>(mapped, this.page, this.size, this.total);
    }

    /** 총 페이지 수 계산(소수점 올림) */
    public int totalPages() {
        return size <= 0 ? 0 : (int) ((total + size - 1) / size);
    }
}
