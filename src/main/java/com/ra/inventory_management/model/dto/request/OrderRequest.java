package com.ra.inventory_management.model.dto.request;

import com.ra.inventory_management.common.EOrderStatus;
import com.ra.inventory_management.model.entity.Users;
import jakarta.persistence.Column;
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
    private EOrderStatus status = EOrderStatus.PENDING;
    private LocalDateTime plannedExportDate;
    private LocalDateTime actualExportDate;
    private String deliveryAddress;
    private String note;
    private String orderCode;
    private List<OrderDetailRequest> orderDetailsRequest;
    private Integer deleteFg;

    //    private Users users;
    public BigDecimal getTotalPrice() {
        return totalPrice != null ? totalPrice : BigDecimal.ZERO;
    }


}
