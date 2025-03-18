package com.ra.inventory_management.model.dto.request;

import lombok.Data;

@Data
public class AreaRequest {
    private Integer id;

    private String name;

    private Float posX;

    private Float posY;

    private Integer capacity;

    private String description;
}
