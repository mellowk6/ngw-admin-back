package com.nhbank.ngw.domain.log.model;

import lombok.Builder;

import java.util.List;

@Builder
public record LogSearchCond(
        String guid,
        List<String> logger,
        Integer page,
        Integer size
) {
    public int pageOrDefault() {
        return page == null ? 0 : page;
    }

    public int sizeOrDefault() {
        return size == null ? 10 : size;
    }

    public int offset() {
        return pageOrDefault() * sizeOrDefault();
    }

    public int limit() {
        return sizeOrDefault();
    }
}