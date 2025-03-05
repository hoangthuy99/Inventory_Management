package com.ra.inventory_management.model.dto.request;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseOrderRequest {
    private Integer id;

    private Integer supplierId;

    private LocalDateTime orderDate;

    private Integer branchId;

    private LocalDateTime orderDatePlan;

    private Integer status;

    private String note;

    private List<PurchaseOrderItemRequest> items;
}
