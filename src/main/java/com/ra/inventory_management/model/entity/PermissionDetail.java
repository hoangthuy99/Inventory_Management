package com.ra.inventory_management.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PermissionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission; // Quyền cha

    @Column(name = "action", nullable = false)
    private String action; // Hành động cụ thể (VD: "CREATE", "READ", "UPDATE", "DELETE")
}
