package com.ra.inventory_management.model.dto.request;

import com.ra.inventory_management.common.EOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderRequest {
    private Long customerId;
    private Long branchId;
    private BigDecimal totalPrice;
    private EOrderStatus status;
    private LocalDateTime plannedExportDate;
    private String orderCode;
    private List<OrderDetailRequest> orderDetails;
    public BigDecimal getTotalPrice() {
        return totalPrice != null ? totalPrice : BigDecimal.ZERO;
    }

}
