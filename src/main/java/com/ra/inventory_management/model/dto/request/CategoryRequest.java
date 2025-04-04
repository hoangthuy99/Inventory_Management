package com.ra.inventory_management.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CategoryRequest {
    @NotBlank(message = "CategoryName không được trống!")
    private String name;
    private String code;
    @NotBlank(message = "Hãy nhập mô tả danh mục!")
    private String description;
    private Integer activeFlag = 1;
    public static String generateUserCode() {
        String code = "CT" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5).toUpperCase();
        System.out.println("Generated User Code: " + code); // Kiểm tra output
        return code;
    }
}
