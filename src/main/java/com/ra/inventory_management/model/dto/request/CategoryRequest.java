package com.ra.inventory_management.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CategoryRequest {
    @NotBlank(message = "CategoryName không được trống!")
    private String name;
    @NotBlank(message = "Hãy nhập mô tả code!")
    @Pattern(regexp = "^[A-Za-z]{2}\\d{3}$", message = "Code phải có 5 ký tự, bắt đầu bằng 2 chữ cái và 3 số!")
    private String code;
    @NotBlank(message = "Hãy nhập mô tả danh mục!")
    private String description;
    private Integer activeFlag = 1;
}
