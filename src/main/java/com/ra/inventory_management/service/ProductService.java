package com.ra.inventory_management.service;


import com.ra.inventory_management.model.dto.request.ProductRequest;
import com.ra.inventory_management.model.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<ProductInfo> getAll(Pageable pageable, String nameSearch);
    ProductInfo save(ProductRequest productRequest);
    ProductInfo findById(Long id);
    void delete(Long id);
    Page<ProductInfo> getByCategoryActiveFlag(Pageable pageable, Integer activeFlag);

    Page<ProductInfo> getByCategoryId(Long id, Pageable pageable);

    List<ProductInfo> searchByName(String keyword);
    Pageable createPageable(int page, int limit, String sort, String order);

}
