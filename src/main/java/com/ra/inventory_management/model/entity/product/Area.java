package com.ra.inventory_management.model.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "area")
public class Area {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Integer activeFlag = 1;

    @Column(nullable = false)
    private String location;


}
