package com.ra.inventory_management.model.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class TotalBussinessResponse {
    private Integer totalProducts;

    private Double totalRevenue;

    private Integer totalBranchs;

    private List<Integer> lowStockProducts;
}
