package com.ra.inventory_management.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

}
