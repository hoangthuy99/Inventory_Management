package com.ra.inventory_management.service;


import com.ra.inventory_management.model.dto.request.ProductRequest;
import com.ra.inventory_management.model.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProductService {
    List<ProductInfo> getAll();

    ProductInfo save(ProductRequest productRequest, String imagePath,Long id);

    ProductInfo findById(Long id);


    void delete(Long id);

    Page<ProductInfo> getByCategoryActiveFlag(Pageable pageable, Integer activeFlag);

    Page<ProductInfo> getByCategoryId(Long id, Pageable pageable);

    List<ProductInfo> searchByName(String keyword);

    List<ProductInfo> importExcel(MultipartFile file) throws IOException;

    Map<String, String> getSampleExcel() throws IOException;
}
