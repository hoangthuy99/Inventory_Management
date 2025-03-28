package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.entity.Supplier;
import com.ra.inventory_management.reponsitory.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("app/supplier")
public class SupplierController {
    @Autowired
    private SupplierRepository supplierRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("getAllSuppliers")
    public ResponseEntity<?> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return ResponseEntity.ok().body(new BaseResponse<>(suppliers));
    }
}
