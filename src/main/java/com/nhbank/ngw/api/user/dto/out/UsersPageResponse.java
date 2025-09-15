package com.nhbank.ngw.api.user.dto.out;

import java.util.List;

/**
 * 프론트가 기대하는 Page<T> 포맷 그대로 반환
 *  - content / totalPages / totalElements / number
 */
public record UsersPageResponse(
        List<UserDto> content,
        int totalPages,
        long totalElements,
        int number
) { }
