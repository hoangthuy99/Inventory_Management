package com.ra.inventory_management.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class CustomerRequest {
    private Long id;
    private String name;
    private String cusCode;
    private String email;
    private String phone;
    private String address;
    private int activeFlag = 1;

}
