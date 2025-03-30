package com.ra.inventory_management.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name; // Tên quyền (VD: "MANAGE_ORDERS", "VIEW_REPORTS")

    @Column(name = "description")
    private String description; // Mô tả quyền

    @Column(name = "active_flag", nullable = false)
    private Integer activeFlag;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PermissionDetail> permissionDetails; // Danh sách các chức năng cụ thể của quyền
    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Auth> auths;
    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;


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
