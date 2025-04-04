package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.request.OrderRequest;
import com.ra.inventory_management.model.dto.request.PurchaseOrderRequest;
import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.entity.Orders;
import com.ra.inventory_management.model.entity.PurchaseOrder;
import com.ra.inventory_management.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/app/order")
public class OrderController {
    @Autowired
    private OrderService orderService;


    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<List<Orders>> getAll(@RequestParam(required = false) Long customerId) {
        if (customerId != null) {
            return ResponseEntity.ok(orderService.getAllByCus(customerId));
        }
        return ResponseEntity.ok(orderService.getAll());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @PostMapping("/createOrder")
    public ResponseEntity<?> saveOrder(@RequestBody OrderRequest orderRequest) {
        System.out.println("Dữ liệu nhận được: " + orderRequest);
        Orders savedOrder = orderService.save(orderRequest);
        return ResponseEntity.ok().body(new BaseResponse<>(savedOrder));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @PutMapping("/updateOrder")
    public ResponseEntity<?> updateOrder(@RequestBody OrderRequest request) {
        Orders order = orderService.update(request);
        return ResponseEntity.ok().body(new BaseResponse<>(order));
    }


    // Lấy đơn hàng theo ID
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long id) {
        Optional<Orders> order = orderService.findById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Lấy đơn hàng theo danh sách id
    @GetMapping("getByIdList")
    public ResponseEntity<?> getOrderByIdList(@RequestParam("ids") List<Long> ids) {
        List<Orders> orders = orderService.findByIdList(ids);
        return ResponseEntity.ok().body(new BaseResponse<>(orders));
    }

    // Tìm đơn hàng theo ID khách hàng và trạng thái
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @GetMapping("/{customerId}/{orderId}/{status}")
    public ResponseEntity<Orders> getByIdAndStatus(
            @PathVariable Long customerId,
            @PathVariable Long orderId,
            @PathVariable int status) {
        Orders order = orderService.getByIdAndStatus(customerId, orderId, status);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, Integer> requestBody) {
        try {
            Integer newStatus = requestBody.get("newStatus");
            if (newStatus == null) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Thiếu trạng thái mới"));
            }

            Orders updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
            return ResponseEntity.ok(updatedOrder);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Collections.singletonMap("error", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Lỗi hệ thống"));
        }
    }

    // Tìm đơn hàng theo mã đơn hàng
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<List<Orders>> searchByOrderCode(@RequestParam String keyword) {
        return ResponseEntity.ok(orderService.searchByOrderCode(keyword));
    }

    @PutMapping("/restore/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<?> restoreOrder(@PathVariable Long id) {
        orderService.findByDeleteFg(true);
        return ResponseEntity.ok("Đơn hàng đã khôi phục thành công!");
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        orderService.deleteOr(id);
        return ResponseEntity.ok("Đơn hàng đã khôi phục thành công!");
    }
}
