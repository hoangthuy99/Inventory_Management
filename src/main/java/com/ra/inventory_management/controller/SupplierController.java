package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.entity.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ra.inventory_management.service.SupplierService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("app/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @GetMapping("getAll")
    public ResponseEntity<?> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok().body(new BaseResponse<>(suppliers));
    }

    @GetMapping("getSupplier/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable Long id) {
        Supplier supplier = supplierService.getSupplierById(id);
        if (supplier != null) {
            return ResponseEntity.ok().body(new BaseResponse<>(supplier));
        }
        return ResponseEntity.badRequest().body(new BaseResponse<>("Không tìm thấy nhà cung cấp!"));
    }


    @PostMapping("add")
    public ResponseEntity<?> createSupplier(@RequestBody Supplier supplier) {
        Supplier newSupplier = supplierService.createSupplier(supplier);
        return ResponseEntity.ok().body(new BaseResponse<>(newSupplier.getActiveFlag(), "Thêm nhà cung cấp thành công!"));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplierDetails) {
        Supplier updatedSupplier = supplierService.updateSupplier(id, supplierDetails);
        if (updatedSupplier != null) {
            return ResponseEntity.ok().body(new BaseResponse<>(updatedSupplier.getActiveFlag(), "Cập nhật thành công!"));
        }
        return ResponseEntity.badRequest().body(new BaseResponse<>("Không tìm thấy nhà cung cấp để cập nhật!"));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok().body(new BaseResponse<>("Xóa nhà cung cấp thành công!"));
    }
}

