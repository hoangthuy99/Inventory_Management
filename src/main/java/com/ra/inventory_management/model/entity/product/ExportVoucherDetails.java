package com.ra.inventory_management.model.entity.product;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExportVoucherDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductInfo product; // Sản phẩm được xuất

    @Column(nullable = false)
    private int quantity; // Số lượng xuất

    @Column(nullable = false)
    private double price; // Giá xuất mỗi đơn vị sản phẩm

    @Column(nullable = false)
    private double totalAmount; // Tổng tiền (quantity * price)

    @PrePersist
    @PreUpdate
    private void calculateTotalAmount() {
        this.totalAmount = this.quantity * this.price;
    }

}