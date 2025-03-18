package com.ra.inventory_management.model.dto.request;

import lombok.Data;

@Data
public class SearchRequest {
    private String searchKey;

    private Integer status;

    private Integer pageNum = 0;

    private Integer pageSize = 5;

    private String sortBy = "id";

    private String sortType = "desc";
}
