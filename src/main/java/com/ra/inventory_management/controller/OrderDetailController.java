package com.ra.inventory_management.controller;


import com.ra.inventory_management.model.entity.OrderDetails;
import com.ra.inventory_management.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/app/order-details")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    // Lấy danh sách OrderDetails theo productId
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @GetMapping("/{productId}")
    public ResponseEntity<List<OrderDetails>> getOrderDetailsByProductId(@PathVariable Long productId) {
        List<OrderDetails> orderDetails = orderDetailService.getAll(productId);
        return ResponseEntity.ok(orderDetails);
    }

    // Tìm OrderDetails theo tên sản phẩm
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @GetMapping("/search")
    public ResponseEntity<List<OrderDetails>> searchByProductName(@RequestParam String keyword) {
        List<OrderDetails> results = orderDetailService.searchByProductName(keyword);
        return ResponseEntity.ok(results);
    }

    // Thêm mới OrderDetails
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @PostMapping
    public ResponseEntity<OrderDetails> createOrderDetail(@RequestBody OrderDetails orderDetails) {
        OrderDetails savedOrderDetail = orderDetailService.save(orderDetails);
        return ResponseEntity.ok(savedOrderDetail);
    }

    // Xóa OrderDetails theo ID
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetail(@PathVariable Long id) {
        orderDetailService.delete(id);
        return ResponseEntity.ok("OrderDetail đã được xóa thành công!");
    }
}