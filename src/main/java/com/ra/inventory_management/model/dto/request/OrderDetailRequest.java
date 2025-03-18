package com.ra.inventory_management.model.dto.request;

import com.ra.inventory_management.model.entity.Users;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderDetailRequest {
    private Long productId;
    private Integer qty;
    private BigDecimal unitPrice;
    private Integer productUnit;
    private LocalDateTime createdAt;
    //    private Users createdBy;
    private LocalDateTime updatedAt;
    private Boolean deleteFg;
}
