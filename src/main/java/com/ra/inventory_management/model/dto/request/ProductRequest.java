package com.ra.inventory_management.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductRequest {
    @NotEmpty(message = "Không được bỏ trống Product name")
    private String name;
    @Pattern(regexp = "^[A-Za-z]{2}\\d{3}$", message = "Code phải có 5 ký tự, bắt đầu bằng 2 chữ cái và 3 số!")
    private String code;
    @NotEmpty(message = "Không được bỏ trống mô tả sản phẩm")
    private String description;

    @NotNull(message = "Hãy nhập giá sản phẩm")
    @Positive(message = "Giá sản phẩm phải là số dương")
    private Double price;

    @Min(value = 0, message = "Số lượng không được nhỏ hơn 0")
    private int quantity;

    private String image;

    @NotNull(message = "Hãy chọn danh mục sản phẩm")
    private Long categoryId;
    private Integer activeFlag = 1;
}
