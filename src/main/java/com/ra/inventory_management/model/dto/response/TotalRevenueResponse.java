package com.ra.inventory_management.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TotalRevenueResponse {
    private Integer filterType;

    private Double totalRevenue;

    private Double totalImportCost;
}
