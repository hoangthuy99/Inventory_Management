package com.ra.inventory_management.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest {
    private Integer id;
    private String name;
    private String subCode;
    private String email;
    private String phone;
    private String address;
    private Integer activeFlag = 1;
    private LocalDateTime createdDate;

    private LocalDateTime updateDate;
}
