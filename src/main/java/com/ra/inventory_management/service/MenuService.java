package com.ra.inventory_management.service;

import com.ra.inventory_management.model.entity.Menu;

import java.util.Optional;

public interface MenuService {
    Optional<Menu> findById (Long id);

}
