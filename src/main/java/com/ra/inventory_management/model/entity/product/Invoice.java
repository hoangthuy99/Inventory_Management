package com.ra.inventory_management.model.entity.product;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "type")
    private int type;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductInfo productId;

    @Column(name = "qty")
    private int qty;

    @Column(name = "price", precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "active_flag")
    private boolean activeFlag;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;
    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDateTime.now();
    }

}
