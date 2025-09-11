package com.nhbank.ngw.api.user.controller;

import com.nhbank.ngw.domain.user.model.Menu;
import com.nhbank.ngw.domain.user.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    /** 드롭다운용 전체 메뉴 목록 (도메인 모델 그대로) */
    @GetMapping
    public List<Menu> list() {
        return menuService.list();
    }
}