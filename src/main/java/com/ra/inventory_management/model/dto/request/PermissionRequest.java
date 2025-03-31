package com.ra.inventory_management.model.dto.request;

import com.ra.inventory_management.model.entity.Auth;
import com.ra.inventory_management.model.entity.Menu;
import com.ra.inventory_management.model.entity.PermissionDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PermissionRequest {
    private Long id;
    private String name;
    private String description;
    private Integer activeFlag;
    private Set<PermissionDetail> permissionDetails; // Danh sách các chức năng cụ thể của quyền
    private Set<Auth> auths;
    private Menu menu;
}
