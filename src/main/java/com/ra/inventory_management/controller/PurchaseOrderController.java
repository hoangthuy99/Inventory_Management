package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.request.PurchaseOrderRequest;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.entity.PurchaseOrder;
import com.ra.inventory_management.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("app/purchase-order")
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderService purchaseOrderService;


    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @PostMapping("createPurchaseOrder")
    public ResponseEntity<?> createPurchaseOrder(@RequestBody PurchaseOrderRequest request) {
        PurchaseOrder purchaseOrder = purchaseOrderService.createPurchaseOrder(request);
        return ResponseEntity.ok().body(new BaseResponse<>(purchaseOrder));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @PutMapping("updatePurchaseOrder")
    public ResponseEntity<?> updatePurchaseOrder(@RequestBody PurchaseOrderRequest request) {
        PurchaseOrder purchaseOrder = purchaseOrderService.updatePurchaseOrder(request);
        return ResponseEntity.ok().body(new BaseResponse<>(purchaseOrder));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @PostMapping("searchPurchaseOrder")
    public ResponseEntity<?> searchPurchaseOrder(
            @RequestBody SearchRequest request
    ) {
        Page<PurchaseOrder> purchaseOrders = purchaseOrderService.searchPurchaseOrder(request);
        return ResponseEntity.ok().body(new BaseResponse<>(purchaseOrders));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @DeleteMapping("deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        purchaseOrderService.deletePurchase(id);
        return ResponseEntity.ok().body(new BaseResponse<>(null));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @GetMapping("getById/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        PurchaseOrder order = purchaseOrderService.getById(id);
        return ResponseEntity.ok().body(new BaseResponse<>(order));
    }
}


