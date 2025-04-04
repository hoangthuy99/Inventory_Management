package com.ra.inventory_management.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MenuRequest {
    private Long id;

    private String code;

    private String name;

    private String path;

    private String icon;

    private Long parentId;

    private Integer activeFlag = 1;

    private List<Long> roleIds;


}
