package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.entity.*;
import com.ra.inventory_management.reponsitory.MenuRepository;
import com.ra.inventory_management.reponsitory.PermissionRepository;
import com.ra.inventory_management.service.PermissionService;
import com.ra.inventory_management.util.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
permissionRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Page<Permission> search(SearchRequest request) {
        // Lấy thông tin user hiện tại từ context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users userPrincipal = (Users) authentication.getPrincipal();

        // Lấy danh sách quyền mà user đã có thông qua Role → Auth → Permission
        Set<Long> userPermissionIds = userPrincipal.getRoles().stream()
                .flatMap(role -> role.getAuths().stream())
                .map(auth -> auth.getPermission().getId()) // Chỉ lấy ID để so sánh nhanh hơn
                .collect(Collectors.toSet());

        // Lấy danh sách quyền có phân trang từ repository
        Pageable pageable = PageableUtil.create(request.getPageNum(), request.getPageSize(), request.getSortBy(), request.getSortType());
        Page<Permission> permissionsPage = permissionRepository.searchPermission(request.getSearchKey(), request.getStatus(), pageable);

        // Lọc bỏ các quyền mà user đã có
        List<Permission> filteredPermissions = permissionsPage.getContent().stream()
                .filter(permission -> !userPermissionIds.contains(permission.getId())) // So sánh bằng ID nhanh hơn
                .collect(Collectors.toList());

        return new PageImpl<>(filteredPermissions, pageable, filteredPermissions.size());
    }


}
