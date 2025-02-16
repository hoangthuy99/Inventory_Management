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
public class ProductInStockRequest {

    @Pattern(regexp = "^[A-Za-z]{2}\\d{3}$", message = "Code phải có 5 ký tự, bắt đầu bằng 2 chữ cái và 3 số!")
    private String code;


    @Min(value = 0, message = "Số lượng không được nhỏ hơn 0")
    private int quantity;

    @NotNull(message = "Hãy chọn mã  sản phẩm")
    private Long productId;
    @NotNull(message = "Hãy chọn danh mục sản phẩm")
    private Long categoryId;
    private Integer activeFlag = 1;
}
