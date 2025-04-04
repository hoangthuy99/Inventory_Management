package com.ra.inventory_management.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductRequest {
    @NotEmpty(message = "Không được bỏ trống Product name")
    private String name;
    private String code;
    @NotEmpty(message = "Không được bỏ trống mô tả sản phẩm")
    private String description;

    @NotNull(message = "Hãy nhập giá sản phẩm")
    @Positive(message = "Giá sản phẩm phải là số dương")
    private Double price;

    @Min(value = 0, message = "Số lượng không được nhỏ hơn 0")
    private Integer qty;

    private MultipartFile img;

    @NotNull(message = "Hãy chọn danh mục sản phẩm")
    private Long categoryId;
    private Integer activeFlag = 1;
}
