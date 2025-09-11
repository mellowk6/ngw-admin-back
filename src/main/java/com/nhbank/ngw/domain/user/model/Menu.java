package com.nhbank.ngw.domain.user.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    private String menuId;        // MENU_ID
    private String parentId;      // PARENT_ID (루트는 null 혹은 고정값)
    private String menuName;      // MENU_NAME
    private Boolean leaf;         // LEAF_YN → Boolean
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}