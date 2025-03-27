package com.ra.inventory_management.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class TotalRevenueResponse {
    private Object filterType;

    private Double totalRevenue;

    private Double totalImportCost;
}
