package com.ra.inventory_management.service;

import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface SupplierService {
    List<Supplier> getAll();

    Supplier save(Supplier supplier);

    Optional<Supplier> findById(Integer id);

    void delete(Integer id);


    List<Supplier> importExcel(MultipartFile file) throws IOException;

    Map<String, String> getSampleExcel() throws IOException;

    Page<Supplier> searchSupplier(SearchRequest request);
}
