package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.request.SupplierRequest;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.entity.ProductInfo;
import com.ra.inventory_management.model.entity.Supplier;
import com.ra.inventory_management.reponsitory.SupplierRepository;
import com.ra.inventory_management.service.EmailService;
import com.ra.inventory_management.service.SupplierService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private static final Logger logger = LoggerFactory.getLogger(SupplierController.class);

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
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF') or hasRole('MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable Integer id) {
        Optional<Supplier> supplier = supplierService.findById(id);
        if (supplier == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy nhà cung cấp");
        }
        return ResponseEntity.ok(supplier);
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF') or hasRole('MANAGER')")
    @PostMapping("addSupplier")
    public ResponseEntity<?> addSupplier(@RequestBody SupplierRequest supplierRequest) {
        logger.info("Nhận yêu cầu thêm supplier với email: {}", supplierRequest.getEmail());

        try {
            // Kiểm tra và set giá trị mặc định cho activeFlag nếu không có
            if (supplierRequest.getActiveFlag() == null) {
                supplierRequest.setActiveFlag(1);
                logger.debug("Đặt activeFlag mặc định là 1 cho supplier.");
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
            logger.info("Thêm supplier thành công với email: {}", savedSupplier.getEmail());

            // Gửi email chào mừng nhà cung cấp mới
            try {
                String subject = "Chào mừng bạn đến với hệ thống của chúng tôi!";
                String message = "Cảm ơn bạn đã đăng ký với hệ thống của chúng tôi. Mã nhà cung cấp của bạn là: " + generatedCode;
                emailService.sendEmail(supplier.getEmail(), subject, message);
                logger.info("Gửi email chào mừng thành công cho: {}", supplier.getEmail());
            } catch (Exception emailException) {
                logger.error("Gửi email thất bại: {}", emailException.getMessage());
            }

            // Trả về thông tin nhà cung cấp vừa thêm vào
            return ResponseEntity.ok(savedSupplier);

        } catch (DataIntegrityViolationException e) {
            logger.warn("Lỗi trùng email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("error", "Email '" + supplierRequest.getEmail() + "' đã tồn tại!"));
        } catch (IllegalArgumentException e) {
            logger.warn("Lỗi tham số không hợp lệ: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Lỗi hệ thống: {}", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Lỗi hệ thống: " + e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF') or hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(@PathVariable Integer id, @RequestBody SupplierRequest supplierRequest) {
        logger.info("Nhận yêu cầu cập nhật supplier với ID: {}", id);

        if (supplierRequest == null) {
            logger.warn("Dữ liệu yêu cầu không hợp lệ!");
            return ResponseEntity.badRequest().body("Dữ liệu yêu cầu không hợp lệ!");
        }

        Optional<Supplier> existingSupplier = supplierService.findById(id);
        if (existingSupplier.isEmpty()) {
            logger.warn("Không tìm thấy supplier với ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy nhà cung cấp");
        }

        Supplier supplier = existingSupplier.get();
        supplier.setName(supplierRequest.getName());
        supplier.setAddress(supplierRequest.getAddress());
        supplier.setPhone(supplierRequest.getPhone());
        supplier.setEmail(supplierRequest.getEmail());
        supplier.setActiveFlag(supplierRequest.getActiveFlag());

        try {
            supplierService.save(supplier);
            logger.info("Cập nhật supplier thành công với email: {}", supplier.getEmail());

            // Gửi email thông báo đã cập nhật thông tin
            try {
                String subject = "Thông tin nhà cung cấp đã được cập nhật";
                String message = "Chào bạn,\n\nThông tin nhà cung cấp của bạn đã được cập nhật trong hệ thống.\n\n" +
                        "Tên: " + supplier.getName() + "\n" +
                        "Địa chỉ: " + supplier.getAddress() + "\n" +
                        "Số điện thoại: " + supplier.getPhone() + "\n\n" +
                        "Trân trọng,\nHệ thống quản lý kho.";
                emailService.sendEmail(supplier.getEmail(), subject, message);
                logger.info("Gửi email thông báo thành công cho: {}", supplier.getEmail());
            } catch (Exception emailException) {
                logger.error("Gửi email thất bại: {}", emailException.getMessage());
            }

            return ResponseEntity.ok(supplier);

        } catch (DataIntegrityViolationException e) {
            logger.warn("Lỗi trùng email khi cập nhật: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("error", "Email '" + supplierRequest.getEmail() + "' đã tồn tại!"));
        } catch (Exception e) {
            logger.error("Lỗi hệ thống khi cập nhật: {}", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Lỗi hệ thống: " + e.getMessage()));
        }
    }
    //  API xóa
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        logger.info("Nhận yêu cầu xóa supplier với ID: {}", id);

        try {
            supplierService.delete(id);
            return ResponseEntity.ok("Supplier deleted successfully");
        } catch (EntityNotFoundException e) {
            logger.warn("Không tìm thấy supplier để xóa: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Lỗi hệ thống khi xóa: {}", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Lỗi hệ thống: " + e.getMessage()));
        }
    }

    // API import file excel
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
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
