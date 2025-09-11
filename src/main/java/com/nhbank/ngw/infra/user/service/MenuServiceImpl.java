package com.nhbank.ngw.infra.user.service;

import com.nhbank.ngw.domain.user.model.Menu;
import com.nhbank.ngw.domain.user.repository.MenuRepository;
import com.nhbank.ngw.domain.user.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository repo;

    @Override
    public List<Menu> list() {
        return repo.findAll();
    }
}