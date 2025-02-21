package com.ra.inventory_management.model.dto.response;

import com.ra.inventory_management.model.entity.product.Categories;
import com.ra.inventory_management.model.entity.product.History;
import com.ra.inventory_management.model.entity.product.ProductInfo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String img;
    private Integer activeFlag;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private Categories categories;
    private Integer qty;
    private BigDecimal price;
    private Set<History> histories = new HashSet<>();

    public ProductResponse(ProductInfo product) {
        this.id = product.getId();
        this.name = product.getName();
        this.code = product.getCode();
        this.description = product.getDescription();
        this.img = product.getImg();
        this.activeFlag = product.getActiveFlag();
        this.createdDate = product.getCreatedDate();
        this.updateDate = product.getUpdateDate();
        this.categories = product.getCategories();
        this.qty = product.getQty();
        this.price = product.getPrice();
        this.histories = new HashSet<>(product.getHistories());
    }
}
