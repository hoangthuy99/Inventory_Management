package com.ra.inventory_management.service.impl;


import com.ra.inventory_management.common.ERoles;
import com.ra.inventory_management.model.entity.Roles;
import com.ra.inventory_management.reponsitory.RoleRepository;
import com.ra.inventory_management.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceIMPL implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<Roles> findByRoleName(ERoles name) {
        return Optional.ofNullable(roleRepository.findByRoleName(name));
    }

    @Override
    public List<Roles> getAll() {
        return roleRepository.findAll();

    }

    @Override
    public Roles getById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy vai trò với id: " + id));
    }
}
