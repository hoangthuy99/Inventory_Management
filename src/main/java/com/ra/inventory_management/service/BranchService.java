package com.ra.inventory_management.service;

import com.ra.inventory_management.model.entity.Branch;

import java.util.List;
import java.util.Optional;

public interface BranchService {
    List<Branch> getAll();
    Branch save(Branch branch);

    Optional<Branch> findById(Long id);
    void delete(Long id);
    List<Branch> getbyActiveFlag();
    List<Branch> searchByName(String keyword);
}
