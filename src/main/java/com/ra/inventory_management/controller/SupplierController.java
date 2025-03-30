package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.request.SupplierRequest;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.entity.Supplier;
import com.ra.inventory_management.reponsitory.SupplierRepository;
import com.ra.inventory_management.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("app/supplier")
public class SupplierController {
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierService supplierService;

    @GetMapping("getAllSuppliers")
    public ResponseEntity<?> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return ResponseEntity.ok().body(new BaseResponse<>(suppliers));
    }

    @PostMapping("searchSupplier")
    public ResponseEntity<?> searchSuppliers(
            @RequestBody SearchRequest request
    ) {
        Page<Supplier> Suppliers = supplierService.searchSupplier(request);
        return ResponseEntity.ok().body(new BaseResponse<>(Suppliers));
    }

    @PostMapping(value = "/add-supplier", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addsupplier(@Valid @RequestBody SupplierRequest supplierRequest) {
        try {
            if (supplierRequest.getActiveFlag() == null) {
                supplierRequest.setActiveFlag(1);
            }

            // Chuyển đổi supplierRequest thành Suppliers entity
            Supplier supplier = new Supplier();
            supplier.setName(supplierRequest.getName());
            supplier.setSubCode(supplierRequest.getSubCode());
            supplier.setEmail(supplierRequest.getEmail());
            supplier.setAddress(supplierRequest.getAddress());
            supplier.setPhone(supplierRequest.getPhone());

            // Gọi service để lưu
            Supplier savedsupplier = supplierService.save(supplier);

            // Trả về thông tin chi tiết
            return ResponseEntity.ok(savedsupplier);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Lỗi hệ thống, vui lòng thử lại sau!"));
        }
    }


    // API lấy danh mục theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable Integer id) {
        Optional<Supplier> supplier = supplierService.findById(id);
        if (supplier == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy nhà cung cấp");
        }
        return ResponseEntity.ok(supplier);
    }

    //  API cập nhật danh mục
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(@PathVariable Integer id, @RequestBody SupplierRequest supplierRequest) {
        if (supplierRequest == null) {
            return ResponseEntity.badRequest().body("Dữ liệu yêu cầu không hợp lệ!");
        }

        Optional<Supplier> existingSupplier = supplierService.findById(id);
        if (existingSupplier.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy danh mục");
        }

        Supplier supplier = existingSupplier.get();  // Lấy object từ Optional
        supplier.setName(supplierRequest.getName()); // Cập nhật dữ liệu từ request
        supplier.setAddress(supplierRequest.getAddress());
        supplier.setPhone(supplierRequest.getPhone());
        supplier.setEmail(supplierRequest.getEmail());
        supplier.setActiveFlag(supplierRequest.getActiveFlag());

        supplierService.save(supplier);
        return ResponseEntity.ok(supplier);
    }

    //  API xóa danh mục
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Optional<Supplier> supplier = supplierService.findById(id);

        if (supplier.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Supplier not found");
        }

        supplierService.delete(id);
        return ResponseEntity.ok("Supplier deleted successfully");
    }

    // API import file excel
    @PostMapping(value = "importExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        List<Supplier> response = supplierService.importExcel(file);
        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }

    @GetMapping("sampleExcel")
    public ResponseEntity<?> getSampleExcel() throws IOException {
        Map<String, String> response = supplierService.getSampleExcel();
        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }
}
