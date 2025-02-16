package com.ra.inventory_management.model.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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
    private Categories categories;

    @Column(name = "qty", nullable = false)
    private int qty;

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

    @OneToMany(mappedBy = "productInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<History> histories = new HashSet<>();


    public void setImg(String img) {
        this.img = img;
    }
}