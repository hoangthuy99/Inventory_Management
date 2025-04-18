package com.ra.inventory_management.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PasswordRequest {
    private String oldPass;
    private String newPass;
    private String confirmNewPass;
}

