package com.nhbank.ngw.api.log.dto.out;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(
        List<T> content,
        int number,
        int size,
        long totalElements,
        int totalPages
) {
    /** 컨텐츠/페이지 정보로 직접 생성 (Spring Data 의존 없음) */
    public static <T> PageResponse<T> of(List<T> content, int number, int size, long totalElements) {
        int totalPages = size <= 0 ? 0 : (int) Math.ceil(totalElements / (double) size);
        return new PageResponse<>(content, number, size, totalElements, totalPages);
    }

    /** 이미 만들어진 PageResponse를 다른 타입으로 매핑 */
    public static <E, D> PageResponse<D> map(PageResponse<E> src, Function<E, D> mapper) {
        List<D> mapped = src.content().stream().map(mapper).toList();
        return new PageResponse<>(mapped, src.number(), src.size(), src.totalElements(), src.totalPages());
    }

    /** 빈 페이지 헬퍼 */
    public static <T> PageResponse<T> empty(int number, int size) {
        return new PageResponse<>(List.of(), number, size, 0L, 0);
    }
}