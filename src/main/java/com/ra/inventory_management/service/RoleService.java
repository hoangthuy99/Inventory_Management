package com.ra.inventory_management.service;


import com.ra.inventory_management.common.ERoles;
import com.ra.inventory_management.model.entity.Roles;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Roles> findByRoleName(ERoles name);

    List<Roles> getAll();

    Roles getById(Long id);
}
