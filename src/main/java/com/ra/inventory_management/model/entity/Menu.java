
package com.ra.inventory_management.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    private String path; // Đường dẫn API hoặc URL giao diện

    private String icon; // Icon đại diện cho function

    @Column(name = "parent_id")
    private Long parentId; // Function cha (nếu có)

    @Column(name = "active_fg", nullable = false)
    private Boolean activeFlag = true; // Trạng thái kích hoạt

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate = LocalDateTime.now();
}
