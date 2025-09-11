package com.nhbank.ngw.infra.user.repository;

import com.nhbank.ngw.domain.user.model.Menu;
import com.nhbank.ngw.domain.user.repository.MenuRepository;
import com.nhbank.ngw.infra.user.mapper.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepository {

    private final MenuMapper mapper;

    @Override
    public List<Menu> findAll() {
        return mapper.selectAll();
    }
}