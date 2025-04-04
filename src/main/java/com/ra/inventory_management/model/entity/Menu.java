
package com.ra.inventory_management.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // Mã function (VD: "CATEGORY_CREATE", "CATEGORY_DELETE")

    @Column(nullable = false)
    private String name; // Tên function (VD: "Create Category", "Delete Category")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "menu_role",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Roles> roles = new HashSet<>();
    private String path; // Đường dẫn API hoặc URL giao diện

    private String icon; // Icon đại diện cho function

    @Column(name = "parent_id")
    private Long parentId; // Function cha (nếu có)

    @Column(name = "active_fg", nullable = false)
    private Integer activeFlag = 1; // Trạng thái kích hoạt

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate = LocalDateTime.now();
}
