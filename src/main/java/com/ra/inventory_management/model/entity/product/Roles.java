package com.ra.inventory_management.model.entity.product;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "role_name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private ERoles roleName;

    @Column(name = "description")
    private String description;

    @Column(name = "active_flag")
    private Integer activeFlag;

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


    @OneToMany(mappedBy = "roles", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Auth> auths;



}
