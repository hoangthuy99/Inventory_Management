package com.ra.inventory_management.model.dto;

import com.ra.inventory_management.model.entity.product.ProductInfo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "stock_statistics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockStatisticsDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductInfo product;

    @Column(nullable = false)
    private int totalImported; // Tổng nhập

    @Column(nullable = false)
    private int totalExported; // Tổng xuất

    @Column(nullable = false)
    private int stockRemaining; // Tồn kho = tổng nhập - tổng xuất

    @Column(nullable = false)
    private double revenue; // Tổng doanh thu từ xuất hàng

    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;
}
