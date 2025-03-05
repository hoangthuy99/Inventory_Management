package com.ra.inventory_management.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class SupplierRequest {
    private Long id;
    private String name;
    private String supCode;
    private String email;
    private String phone;
    private String address;
    private int activeFlag;
}
