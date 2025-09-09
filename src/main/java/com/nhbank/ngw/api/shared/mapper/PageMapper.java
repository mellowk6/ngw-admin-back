package com.nhbank.ngw.api.shared.mapper;



import com.nhbank.ngw.api.log.dto.out.PageResponse;
import com.nhbank.ngw.domain.shared.model.Page;

import java.util.function.Function;

public final class PageMapper {

    private PageMapper() {}

    /** 도메인 Page<T> -> API PageResponse<R> (항목 매핑 함수 필요) */
    public static <T, R> PageResponse<R> toDto(Page<T> page, Function<? super T, R> mapper) {
        Page<R> mapped = page.map(mapper);
        return toDto(mapped);
    }

    /** 동일 타입 매핑이 필요 없을 때의 축약형 */
    public static <T> PageResponse<T> toDto(Page<T> page) {
        int totalPages = page.totalPages();
        return PageResponse.of(page.content(), page.page(), page.size(), page.total(), totalPages);
    }
}