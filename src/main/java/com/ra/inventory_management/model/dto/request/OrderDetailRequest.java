package com.ra.inventory_management.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderDetailRequest {
    private Long productId;
    private int quantity;
    private BigDecimal unitPrice;
}
