package com.ra.inventory_management.model.dto.request;

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
    private Long id;
    private Long customerId;
    private Long branchId;
    private Integer shipperId;
    private BigDecimal totalPrice;
    private Integer status = 1;
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
