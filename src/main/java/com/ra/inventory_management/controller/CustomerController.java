package com.ra.inventory_management.controller;


import com.ra.inventory_management.model.entity.Customer;
import com.ra.inventory_management.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/app/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Lấy danh sách tất cả khách hàng
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
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

    // Thêm mới khách hàng
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.save(customer);
        return ResponseEntity.ok(savedCustomer);
    }

    // Cập nhật thông tin khách hàng
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        Optional<Customer> existingCustomer = customerService.findById(id);
        if (existingCustomer.isPresent()) {
            customer.setId(id); // Đảm bảo ID không bị thay đổi
            Customer updatedCustomer = customerService.save(customer);
            return ResponseEntity.ok(updatedCustomer);
        }
        return ResponseEntity.notFound().build();
    }

    // Xóa khách hàng theo ID
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
    @GetMapping("/search")
    public ResponseEntity<List<Customer>> searchCustomers(@RequestParam String keyword) {
        List<Customer> customers = customerService.searchByName(keyword);
        return ResponseEntity.ok(customers);
    }
}
