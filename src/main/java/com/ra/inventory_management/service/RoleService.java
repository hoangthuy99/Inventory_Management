package com.ra.inventory_management.service;



import com.ra.inventory_management.model.entity.ERoles;
import com.ra.inventory_management.model.entity.Roles;

import java.util.List;

public interface RoleService {
    Roles findByRoleName(ERoles name);
    List<Roles> getAll();
}
