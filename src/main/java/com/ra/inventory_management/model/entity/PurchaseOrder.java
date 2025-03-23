package com.ra.inventory_management.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Table(name = "purchase_order")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "code")
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @NotNull
    @Column(name = "order_date_plan", nullable = false)
    private LocalDateTime orderDatePlan;

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "note")
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "delete_fg", nullable = false)
    private Boolean deleteFg;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    @Where(clause = "delete_fg = false OR delete_fg IS NULL")
    private List<PurchaseOrderItem> purchaseOrderItems;

}