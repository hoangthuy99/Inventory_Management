package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.model.entity.Menu;
import com.ra.inventory_management.reponsitory.MenuRepository;
import com.ra.inventory_management.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@RequiredArgsConstructor
@Service
public class MenuServiceIMPL implements MenuService {
    private final MenuRepository functionRepository;
    @Override
    public Optional<Menu> findById(Long id) {
        return functionRepository.findById(id);
    }
}
