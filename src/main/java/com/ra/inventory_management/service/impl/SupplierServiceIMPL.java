package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.common.Constant;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.entity.Categories;
import com.ra.inventory_management.model.entity.Supplier;
import com.ra.inventory_management.reponsitory.SupplierRepository;
import com.ra.inventory_management.service.SupplierService;
import com.ra.inventory_management.util.ExcelUtil;
import com.ra.inventory_management.util.PageableUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class SupplierServiceIMPL implements SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ExcelUtil excelUtil;

    @Override
    public List<Supplier> getAll() {
        return supplierRepository.findAll();
    }

    @Override
    public Supplier save(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public Optional<Supplier> findById(Integer id) {
        return supplierRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        supplierRepository.deleteById(id);
    }


    @Override
    public List<Supplier> importExcel(MultipartFile file) throws IOException {
        return null;
    }

    @Override
    public Map<String, String> getSampleExcel() throws IOException {
        try {
            // Get file from resource
            ClassPathResource pathResource = new ClassPathResource(Constant.SUPPLIER_SAMPLE);
            if (!pathResource.exists()) {
                throw new FileNotFoundException("Không tìm thấy file: " + pathResource.getPath());
            }

            // Copy file to temp folder
            Path tempFile = Files.createTempFile("sample_supplier", ".xlsx");
            Files.copy(pathResource.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            // Convert base64
            byte[] fileContent = Files.readAllBytes(tempFile);
            String base64Encoded = Base64.encodeBase64String(fileContent);


            // Remove file temp after write
            Files.deleteIfExists(tempFile);

            Map<String, String> response = new HashMap<>();
            response.put("fileName", "sample_supplier");
            response.put("base64", "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64," + base64Encoded);

            return response;
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public Page<Supplier> searchSupplier(SearchRequest request) {
        log.info("start: searchSupplier");

        Pageable pageable = PageableUtil.create(request.getPageNum(), request.getPageSize(), request.getSortBy(), request.getSortType());

        Page<Supplier> supplier = supplierRepository.searchSupplier(request.getSearchKey(), request.getStatus(), pageable);

        log.info("end: searchSupplier");

        return supplier;
    }
}

