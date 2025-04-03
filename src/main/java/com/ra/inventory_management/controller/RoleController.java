package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.entity.Roles;
import com.ra.inventory_management.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/role")
public class RoleController {
   
    
    @Autowired
    private RoleService roleService;


    @GetMapping("/")
    public ResponseEntity<?> getAllRoles() {
        List<Roles> roles = roleService.getAll();
        return ResponseEntity.ok().body(new BaseResponse<>(roles));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Roles Roles = roleService.getById(id);
        return ResponseEntity.ok().body(new BaseResponse<>(Roles));
    }

}
