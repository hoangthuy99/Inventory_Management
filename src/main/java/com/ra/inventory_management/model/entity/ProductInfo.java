package com.ra.inventory_management.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class ProductInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_name", length = 100, unique = true, nullable = false)
    private String name;

    @Column(name = "code", length = 50, unique = true, nullable = false)
    private String code;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "img", length = 255)
    private String img;

    @Column(name = "active_flag", nullable = false)
    private Integer activeFlag;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cate_id", nullable = false)
    @ToString.Exclude
    private Categories categories;

    @Column(name = "qty", nullable = false)
    private Integer qty;

    @Column(name = "price", precision = 15, scale = 2, nullable = false)
    private BigDecimal price;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDateTime.now();
    }

    public void setImg(String img) {
        this.img = img;
    }

    public static String generateOrderCode() {
        return "PD" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8).toUpperCase();
    }
}
