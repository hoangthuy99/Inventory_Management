package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.common.Constant;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.entity.Supplier;
import com.ra.inventory_management.reponsitory.SupplierRepository;
import com.ra.inventory_management.service.SupplierService;
import com.ra.inventory_management.util.ExcelUtil;
import com.ra.inventory_management.util.PageableUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
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
    private static final Logger logger = LoggerFactory.getLogger(SupplierServiceIMPL.class);

    @Override
    public List<Supplier> getAll() {
        return supplierRepository.findAll();
    }

    @Override
    public Supplier save(Supplier supplier) {
        if (supplier.getEmail() != null) {
            logger.info("Kiểm tra email tồn tại: {}", supplier.getEmail());
            if (supplierRepository.existsByEmailAndActiveFlag(supplier.getEmail(), 1)) {
                logger.warn("Email đã tồn tại: {}", supplier.getEmail());
                throw new DataIntegrityViolationException("Email '" + supplier.getEmail() + "' đã tồn tại!");
            }
        }
        Supplier savedSupplier = supplierRepository.save(supplier);
        logger.info("Lưu supplier thành công: {}", savedSupplier.getEmail());
        return savedSupplier;
    }

    @Override
    public Optional<Supplier> findById(Integer id) {
        return supplierRepository.findByIdAndActiveFlag(id, 1);
    }

    @Override
    public void delete(Integer id) {
        Optional<Supplier> supplierOpt = supplierRepository.findByIdAndActiveFlag(id, 1);
        if (supplierOpt.isPresent()) {
            Supplier supplier = supplierOpt.get();
            supplier.setActiveFlag(0); // Đánh dấu xóa mềm
            supplierRepository.save(supplier);
            logger.info("Đánh dấu xóa mềm supplier với ID: {}", id);
        } else {
            logger.warn("Không tìm thấy supplier với ID: {}", id);
            throw new EntityNotFoundException("Không tìm thấy nhà cung cấp với ID: " + id);
        }
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

