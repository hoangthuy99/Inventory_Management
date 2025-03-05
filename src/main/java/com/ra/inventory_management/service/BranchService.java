package com.ra.inventory_management.service;

import com.ra.inventory_management.model.entity.Branch;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BranchService {
    List<Branch> getAll();

    Branch save(Branch branch);

    Optional<Branch> findById(Long id);

    void delete(Long id);

    List<Branch> getbyActiveFlag();

    List<Branch> searchByName(String keyword);

    List<Branch> importExcel(MultipartFile file) throws IOException;

    Map<String, String> getSampleExcel() throws IOException;
}
