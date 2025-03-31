package com.ra.inventory_management.service;

import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.entity.Permission;
import com.ra.inventory_management.model.entity.Users;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PermissionService {
    List<Permission> getAllPermissions() ;


    Optional<Permission> getPermissionById(Long id) ;

    // Tìm quyền theo tên
    Optional<Permission> getPermissionByName(String name);
    // Thêm quyền mới
  Permission createPermission(Permission permission) ;

    // Cập nhật quyền
   Permission updatePermission(Long id, Permission newPermission) ;
    // Xóa quyền
   void deletePermission(Long id) ;
    Page<Permission> search(SearchRequest request);
}
