package com.ra.inventory_management.service;


import com.ra.inventory_management.model.dto.request.ProductRequest;
import com.ra.inventory_management.model.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<ProductInfo> getAll();
    ProductInfo save(ProductRequest productRequest, String imagePath);
    ProductInfo findById(Long id);
    void delete(Long id);
    Page<ProductInfo> getByCategoryActiveFlag(Pageable pageable, Integer activeFlag);
    Page<ProductInfo> getByCategoryId(Long id, Pageable pageable);
    List<ProductInfo> searchByName(String keyword);


}
