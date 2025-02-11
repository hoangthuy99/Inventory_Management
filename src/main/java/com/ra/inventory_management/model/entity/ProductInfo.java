package com.ra.inventory_management.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "img", length = 200)
    private String img;

    @Column(name = "active_flag", nullable = false)
    private Integer activeFlag;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cate_id")
    private Categories categories;

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

    @OneToMany(mappedBy = "productInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductInStock> productInStocks = new HashSet<>();


}
