package com.ra.inventory_management.controller;


import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.entity.Customer;
import com.ra.inventory_management.service.CustomerService;
import com.ra.inventory_management.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/app/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmailService emailService;

    // Lấy danh sách tất cả khách hàng
    @GetMapping
    public ResponseEntity<List<Customer>> getCustomer() {
        List<Customer> customers = customerService.getAll();
        return ResponseEntity.ok(customers);
    }

    // Lấy thông tin khách hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.findById(id);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        try {
            // Sinh mã khách hàng
            String generatedCode = Customer.generateOrderCode();
            customer.setCusCode(generatedCode);
            customer.setActiveFlag(1); // Đặt cờ hoạt động mặc định là 1
            Customer newCustomer = customerService.save(customer);
            // Gửi email chào mừng
            String subject = "Chào mừng bạn đến với hệ thống!";
            String message = "Xin chào " + newCustomer.getName() + ",\n\n" +
                    "Cảm ơn bạn đã đăng ký với chúng tôi.\n" +
                    "Mã khách hàng của bạn là: " + generatedCode;

            emailService.sendEmail(newCustomer.getEmail(), subject, message);

            return ResponseEntity.ok(newCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Không thể tạo khách hàng: " + e.getMessage()));
        }
    }

    // Cập nhật thông tin khách hàng + gửi email
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        Optional<Customer> existingCustomer = customerService.findById(id);
        if (existingCustomer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy khách hàng");
        }

        try {
            customer.setId(id); // Đảm bảo không đổi ID
            Customer updatedCustomer = customerService.save(customer);

            // Gửi email thông báo cập nhật
            String subject = "Cập nhật thông tin khách hàng";
            String message = "Xin chào " + updatedCustomer.getName() + ",\n\n" +
                    "Thông tin của bạn đã được cập nhật thành công:\n" +
                    "Tên: " + updatedCustomer.getName() + "\n" +
                    "Email: " + updatedCustomer.getEmail() + "\n" +
                    "SĐT: " + updatedCustomer.getPhone();

            emailService.sendEmail(updatedCustomer.getEmail(), subject, message);

            return ResponseEntity.ok(updatedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Cập nhật thất bại: " + e.getMessage()));
        }
    }

    // Xóa khách hàng theo ID
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()) {
            customerService.delete(id);
            return ResponseEntity.ok().body("Xóa khách hàng thành công!");
        }
        return ResponseEntity.notFound().build();
    }

    // Tìm kiếm khách hàng theo tên
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @PostMapping("searchCus")
    public ResponseEntity<?> searchCus(@Valid @RequestBody SearchRequest request) {
        Page<Customer> customer = customerService.searchCus(request);
        return ResponseEntity.ok().body(new BaseResponse<>(customer));
    }


    // API import file excel
    @PostMapping(value = "importExcel", produces = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> importExcel(
            @RequestParam("file") MultipartFile file
    )
            throws IOException {
        List<Customer> response = customerService.importExcel(file);
        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }

    @GetMapping("sampleExcel")
    public ResponseEntity<?> getSampleExcel() throws IOException {
        Map<String, String> response = customerService.getSampleExcel();
        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }
}
