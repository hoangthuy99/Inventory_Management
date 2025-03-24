package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.request.OrderRequest;
import com.ra.inventory_management.model.dto.request.PurchaseOrderRequest;
import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.entity.Orders;
import com.ra.inventory_management.model.entity.PurchaseOrder;
import com.ra.inventory_management.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/app/order")
public class OrderController {
    @Autowired
    private OrderService orderService;


    @GetMapping("/")
    public ResponseEntity<List<Orders>> getAll(@RequestParam(required = false) Long customerId) {
        if (customerId != null) {
            return ResponseEntity.ok(orderService.getAllByCus(customerId));
        }
        return ResponseEntity.ok(orderService.getAll());
    }

    @PostMapping("/createOrder")
    public ResponseEntity<Orders> saveOrder(@RequestBody OrderRequest orderRequest) {
        System.out.println("Dữ liệu nhận được: " + orderRequest);
        Orders savedOrder = orderService.save(orderRequest);
        return ResponseEntity.ok(savedOrder);
    }
    @PutMapping("/updateOrder")
    public ResponseEntity<?> updateOrder( @RequestBody OrderRequest request) {
        Orders order = orderService.update(request);
        return ResponseEntity.ok().body(new BaseResponse<>(order));
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
            @PathVariable int status) {
        Orders order = orderService.getByIdAndStatus(customerId, orderId, status);
        return ResponseEntity.ok(order);
    }

    // Tìm đơn hàng theo mã đơn hàng
    @GetMapping("/search")
    public ResponseEntity<List<Orders>> searchByOrderCode(@RequestParam String keyword) {
        return ResponseEntity.ok(orderService.searchByOrderCode(keyword));
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restoreOrder(@PathVariable Long id) {
        orderService.findByDeleteFg(true);
        return ResponseEntity.ok("Đơn hàng đã khôi phục thành công!");
    }


}
