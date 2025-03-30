package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.model.entity.Menu;
import com.ra.inventory_management.model.entity.Permission;
import com.ra.inventory_management.model.entity.PermissionDetail;
import com.ra.inventory_management.reponsitory.MenuRepository;
import com.ra.inventory_management.reponsitory.PermissionRepository;
import com.ra.inventory_management.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionServiceIMPL implements PermissionService {

    private final PermissionRepository permissionRepository;

    private final MenuRepository functionRepository;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Optional<Permission> getPermissionById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public Optional<Permission> getPermissionByName(String name) {
        return permissionRepository.findByName(name);
    }

    @Override
    public Permission createPermission(Permission permission) {
        if (permission.getMenu() == null || permission.getMenu().getId() == null) {
            throw new IllegalArgumentException("Menu ID must not be null");
        }

        if (permission.getPermissionDetails() != null) {
            for (PermissionDetail detail : permission.getPermissionDetails()) {
                detail.setPermission(permission);
            }
        }

        return permissionRepository.save(permission);
    }

    @Override
    public Permission updatePermission(Long id, Permission newPermission) {
        return permissionRepository.findById(id).map(permission -> {
            permission.setName(newPermission.getName());
            permission.setDescription(newPermission.getDescription());
            permission.setActiveFlag(newPermission.getActiveFlag());
            return permissionRepository.save(permission);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy quyền với ID: " + id));
    }

    @Override
    public void deletePermission(Long id) {

    }
}
