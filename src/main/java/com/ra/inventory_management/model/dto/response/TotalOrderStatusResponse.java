package com.ra.inventory_management.model.dto.response;

import lombok.Data;

@Data
public class TotalOrderStatusResponse {
    private Integer totalOrderDone;

    private Integer totalOrderCancel;

    private Integer totalOrderPending;
}
