package com.ra.inventory_management.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Table(name = "area")
@Entity
public class Area {
    @Id
    @Column(name = "id", nullable = false)
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "active_flag", nullable = false)
    private Integer activeFlag;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 100)
    @NotNull
    @Field(type = FieldType.Text)
    private String name;

    @Column(name = "posX")
    private Float posX;

    @Column(name = "posY")
    private Float posY;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "branch_id", insertable = false, updatable = false)
    private Integer branchId;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "branch_id")
    private Branch branch;
}