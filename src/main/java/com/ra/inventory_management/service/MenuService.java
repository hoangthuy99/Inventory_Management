package com.ra.inventory_management.service;

import com.ra.inventory_management.model.dto.request.MenuRequest;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.entity.Menu;
import com.ra.inventory_management.model.entity.Permission;
import com.ra.inventory_management.model.entity.Users;
import org.springframework.data.domain.Page;


import java.util.List;
import java.util.Optional;

public interface MenuService {
    Optional<Menu> findById (Long id);
    List<Menu> getAll();
    void delete(Long id);

    Menu save(Menu menu);

    Menu update(MenuRequest request, Long id);
    Page<Menu> search(SearchRequest request);

}
