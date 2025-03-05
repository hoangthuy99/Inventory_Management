package com.ra.inventory_management.model.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseOrderItemRequest {
    private Integer id;

    private Long productId;

    private Integer areaId;

    private Integer itemUnit;

    private Integer stockType;

    private Integer quantityPlan;

    private Integer quantityActual;
}
