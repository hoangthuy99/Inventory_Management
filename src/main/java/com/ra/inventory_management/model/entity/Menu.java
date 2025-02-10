package com.ra.inventory_management.model.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    private int parentId;

    @Column(name = "url", length = 100, nullable = false)
    private String url;

    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    @Column(name = "active_flag")
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

    @OneToMany(mappedBy = "menu", fetch = FetchType.EAGER)
    private Set<Auth> auths = new HashSet<>();

    @Transient
    private List<Menu> child;
}

