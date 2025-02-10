package com.ra.inventory_management.service.impl;


import com.ra.inventory_management.model.entity.ERoles;
import com.ra.inventory_management.model.entity.Roles;
import com.ra.inventory_management.repository.RoleRepository;
import com.ra.inventory_management.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceIMPL implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Roles findByRoleName(ERoles name) {
        return roleRepository.findByRoleName(name);
    }

    @Override
    public List<Roles> getAll() {
        return roleRepository.findAll();
    }

}
