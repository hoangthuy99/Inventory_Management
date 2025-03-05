package com.ra.inventory_management.service;

import com.ra.inventory_management.model.entity.Supplier;

import java.util.List;

public interface SupplierService {
    List<Supplier> getAllSuppliers();
    Supplier getSupplierById(Long id);
   Supplier createSupplier(Supplier supplier);
    Supplier updateSupplier(Long id, Supplier supplierDetails);
    void deleteSupplier(Long id);

    List<Supplier> searchByName(String keyword);
}
