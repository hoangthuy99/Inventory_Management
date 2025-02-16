package com.ra.inventory_management.model.entity.product;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductInfo product;
    @Column(nullable = false, length = 50)
    private String color;
    @Column(nullable = false)
    private int importPrice;

    @Column(nullable = false)
    private int exportPrice;

    @Column(nullable = false)
    private int totalInStock;

}
