package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.request.SupplierRequest;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.entity.ProductInfo;
import com.ra.inventory_management.model.entity.Supplier;
import com.ra.inventory_management.reponsitory.SupplierRepository;
import com.ra.inventory_management.service.EmailService;
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
    @Autowired
    private EmailService emailService;

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

    // API lấy danh mục theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable Integer id) {
        Optional<Supplier> supplier = supplierService.findById(id);
        if (supplier == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy nhà cung cấp");
        }
        return ResponseEntity.ok(supplier);
    }

    @PostMapping("addSupplier")
    public ResponseEntity<?> addSupplier(@RequestBody SupplierRequest supplierRequest) {
        try {
            // Kiểm tra và set giá trị mặc định cho activeFlag nếu không có
            if (supplierRequest.getActiveFlag() == null) {
                supplierRequest.setActiveFlag(1);
            }

            // Chuyển đổi SupplierRequest thành entity Supplier
            Supplier supplier = new Supplier();
            supplier.setName(supplierRequest.getName());
            String generatedCode = Supplier.generateOrderCode();
            supplier.setSubCode(generatedCode);
            supplier.setEmail(supplierRequest.getEmail());
            supplier.setAddress(supplierRequest.getAddress());
            supplier.setPhone(supplierRequest.getPhone());
            supplier.setActiveFlag(supplierRequest.getActiveFlag());

            // Lưu thông tin nhà cung cấp vào cơ sở dữ liệu
            Supplier savedSupplier = supplierService.save(supplier);

            // Gửi email chào mừng nhà cung cấp mới
            String subject = "Chào mừng bạn đến với hệ thống của chúng tôi!";
            String message = "Cảm ơn bạn đã đăng ký với hệ thống của chúng tôi. Mã nhà cung cấp của bạn là: " + generatedCode;
            emailService.sendEmail(supplier.getEmail(), subject, message);

            // Trả về thông tin nhà cung cấp vừa thêm vào
            return ResponseEntity.ok(savedSupplier);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Lỗi hệ thống, vui lòng thử lại sau!"));
        }
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
        supplier.setName(supplierRequest.getName());
        supplier.setAddress(supplierRequest.getAddress());
        supplier.setPhone(supplierRequest.getPhone());
        supplier.setEmail(supplierRequest.getEmail());
        supplier.setActiveFlag(supplierRequest.getActiveFlag());

        supplierService.save(supplier);

        //  Gửi email thông báo đã cập nhật thông tin
        try {
            String subject = "Thông tin nhà cung cấp đã được cập nhật";
            String message = "Chào bạn,\n\nThông tin nhà cung cấp của bạn đã được cập nhật trong hệ thống.\n\n" +
                    "Tên: " + supplier.getName() + "\n" +
                    "Địa chỉ: " + supplier.getAddress() + "\n" +
                    "Số điện thoại: " + supplier.getPhone() + "\n\n" +
                    "Trân trọng,\nHệ thống quản lý kho.";
            emailService.sendEmail(supplier.getEmail(), subject, message);
        } catch (Exception e) {
            // Nếu gửi mail lỗi, ghi log nhưng vẫn trả về thành công
            System.err.println("Gửi email thất bại: " + e.getMessage());
        }

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
