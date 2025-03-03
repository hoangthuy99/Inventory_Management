package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.model.entity.Branch;
import com.ra.inventory_management.reponsitory.BranchRepository;
import com.ra.inventory_management.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchServiceIMPL implements BranchService {
    @Autowired
    private BranchRepository branchRepository;

    @Override
    public List<Branch> getAll() {
        return branchRepository.findAll();
    }

    @Override
    public Branch save(Branch branch) {
        return branchRepository.save(branch);
    }

    @Override
    public Optional<Branch> findById(Long id) {
        return branchRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        branchRepository.deleteById(id);
    }

    @Override
    public List<Branch> getbyActiveFlag() {
        return null;
    }

    @Override
    public List<Branch> searchByName(String keyword) {
        return branchRepository.searchByName(keyword);
    }
}
