package com.ra.inventory_management.service.impl;




import com.ra.inventory_management.model.dto.request.ProductRequest;
import com.ra.inventory_management.model.entity.product.Categories;
import com.ra.inventory_management.model.entity.product.ProductInfo;
import com.ra.inventory_management.reponsitory.CategoryRepository;
import com.ra.inventory_management.reponsitory.ProductRepository;
import com.ra.inventory_management.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;


@Service
public class ProductServiceIMPL implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Override
    public List<ProductInfo> getAll() {
        return productRepository.findAll();
    }

    @Override
    public ProductInfo save(ProductRequest productRequest, String imagePath) {
        try {
            if (productRepository.existsByName(productRequest.getName())) {
                throw new IllegalArgumentException("Tên sản phẩm đã tồn tại, vui lòng nhập tên sản phẩm khác!");
            }

            if (productRequest.getCategoryId() == null) {
                throw new IllegalArgumentException("ID danh mục không được để trống");
            }

            Categories category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("ID danh mục không hợp lệ"));

            ProductInfo product = new ProductInfo();
            product.setName(productRequest.getName());
            product.setCode(productRequest.getCode());
            product.setQty(productRequest.getQty());
            product.setPrice(BigDecimal.valueOf(productRequest.getPrice()));
            product.setDescription(productRequest.getDescription());
            product.setActiveFlag(productRequest.getActiveFlag() != null ? productRequest.getActiveFlag() : 1);
            product.setCategories(category);
            product.setImg(imagePath);

            return productRepository.save(product); // Lưu vào cơ sở dữ liệu
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: ", e); // Log lỗi chi tiết
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Lỗi khi lưu sản phẩm", e); // Log lỗi chi tiết
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống, vui lòng thử lại!", e);
        }
    }

        @Override
    public ProductInfo findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<ProductInfo> getByCategoryActiveFlag(Pageable pageable, Integer activeFlag) {
        return productRepository.findByCategoriesAndActiveFlag(pageable, activeFlag);
    }

    @Override
    public Page<ProductInfo> getByCategoryId(Long id, Pageable pageable) {
        return productRepository.findByCategoryId(id, pageable);
    }


    @Override
    public List<ProductInfo> searchByName(String keyword) {
        return productRepository.searchProductByName(keyword);
    }



}
