package com.ra.inventory_management.model.dto.response;

import lombok.Data;

import java.util.Map;

@Data
public class TotalRevenueResponse {
    private Integer filterType;

    private Double total;
}
