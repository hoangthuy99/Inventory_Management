package com.ra.inventory_management.service;

import com.ra.inventory_management.model.dto.request.PurchaseOrderRequest;
import com.ra.inventory_management.model.entity.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderService {
    PurchaseOrder createPurchaseOrder(PurchaseOrderRequest request);
    PurchaseOrder updatePurchaseOrder(PurchaseOrderRequest request);
    List<PurchaseOrder> searchPurchaseOrder();
    void deletePurchase(Long id);
    PurchaseOrder getById(Long id);
}
