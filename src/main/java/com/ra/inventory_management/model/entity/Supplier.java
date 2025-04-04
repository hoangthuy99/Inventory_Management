package com.ra.inventory_management.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "supplier")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;
    @Column(name = "sub_code", length = 10, unique = true, nullable = false)
    private String subCode;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 15, unique = true)
    private String phone;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(name = "active_flag", nullable = false)
    private int activeFlag;

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

    public static String generateOrderCode() {
        return "SP" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8).toUpperCase();
    }
}
