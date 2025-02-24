package com.ra.inventory_management.controller;

import com.ra.inventory_management.common.EOrderStatus;
import com.ra.inventory_management.model.entity.Customer;
import com.ra.inventory_management.model.entity.Orders;
import com.ra.inventory_management.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/app/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    // Lấy danh sách đơn hàng theo customerId
    @GetMapping("/all/{customerId}")
    public ResponseEntity<List<Orders>> getAllOrders(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getAll(customerId));
    }

    // Thêm đơn hàng mới
    @PostMapping("/add")
    public ResponseEntity<Orders> addOrder(@RequestBody Customer customer, @RequestParam BigDecimal totalPrice) {
        Orders newOrder = orderService.add(customer, totalPrice);
        return ResponseEntity.ok(newOrder);
    }

    // Lưu hoặc cập nhật đơn hàng
    @PostMapping("/save")
    public ResponseEntity<Orders> saveOrder(@RequestBody Orders orders) {
        Orders savedOrder = orderService.save(orders);
        return ResponseEntity.ok(savedOrder);
    }

    // Lấy đơn hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long id) {
        Optional<Orders> order = orderService.findById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Tìm đơn hàng theo ID khách hàng và trạng thái
    @GetMapping("/{customerId}/{orderId}/{status}")
    public ResponseEntity<Orders> getByIdAndStatus(
            @PathVariable Long customerId,
            @PathVariable Long orderId,
            @PathVariable EOrderStatus status) {
        Orders order = orderService.getByIdAndStatus(customerId, orderId, status);
        return ResponseEntity.ok(order);
    }

    // Tìm đơn hàng theo mã đơn hàng
    @GetMapping("/search")
    public ResponseEntity<List<Orders>> searchByOrderCode(@RequestParam String keyword) {
        return ResponseEntity.ok(orderService.searchByOrderCode(keyword));
    }
}
