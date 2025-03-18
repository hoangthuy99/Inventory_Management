package com.ra.inventory_management.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.inventory_management.common.EOrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_code", length = 10, unique = true, nullable = false)
    private String orderCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "total_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EOrderStatus status = EOrderStatus.PENDING;

    @Column(name = "planned_export_date", nullable = false)
    private LocalDateTime plannedExportDate;

    @Column(name = "actual_export_date")
    private LocalDateTime actualExportDate;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @Column(name = "note")
    private String note;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "update_date")
    @JsonIgnore
    private LocalDateTime updateDate;

    @ColumnDefault("b'0'")
    @Column(name = "delete_fg")
    private Boolean deleteFg;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Where(clause = "delete_fg = false OR delete_fg IS NULL")
    private List<OrderDetails> orderDetails;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        calculateTotalPrice();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDateTime.now();
        calculateTotalPrice();
    }

    public static String generateOrderCode() {
        return "OD" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8).toUpperCase();
    }

    public void calculateTotalPrice() {
        if (orderDetails != null && !orderDetails.isEmpty()) {
            this.totalPrice = orderDetails.stream()
                    .map(orderDetail -> orderDetail.getUnitPrice().multiply(BigDecimal.valueOf(orderDetail.getProductInfo().getQty())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.totalPrice = BigDecimal.ZERO;
        }
    }
}
