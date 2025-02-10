package com.ra.inventory_management.service.impl;



import com.ra.inventory_management.model.dto.request.ProductRequest;
import com.ra.inventory_management.model.entity.Categories;
import com.ra.inventory_management.model.entity.ProductInfo;
import com.ra.inventory_management.repository.CategoryRepository;
import com.ra.inventory_management.repository.ProductRepository;
import com.ra.inventory_management.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceIMPL implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;



    @Override
    public Page<ProductInfo> getAll(Pageable pageable, String nameSearch) {
        return productRepository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductRequest productRequest) {

        if (productRepository.existsByName(productRequest.getName())) {
            throw new IllegalArgumentException("Tên sản phẩm đã tồn tại, vui lòng nhập tên sản phẩm khác!");
        }

        // Tạo mới đối tượng Product và lưu vào database
        ProductInfo product = new ProductInfo();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());

        Categories category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        product.setCategories(category);
        product.setImg(productRequest.getImage());

        return productRepository.save(product);
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
    public List<ProductInfo> getByNameOrDes(String name, String description) {
        return productRepository.findByNameOrDescription(name, description);
    }

    @Override
    public Page<ProductInfo> getByCategoryStatus(Pageable pageable, Integer activeFlag) {
        return productRepository.findByCategoriesAndActiveFlag(pageable, activeFlag);
    }

    @Override
    public Page<ProductInfo> getByCategoryId(Long id, Pageable pageable) {
        return productRepository.findByCategoryId(id,pageable);
    }


    @Override
    public List<ProductInfo> searchByName(String keyword) {
        return productRepository.searchProductByName(keyword);
    }

    @Override
    public Pageable createPageable(int page, int limit, String sort, String order) {
        Sort sortOrder;
        if ("asc".equalsIgnoreCase(order)) {
            sortOrder = Sort.by(sort).ascending();
        } else if ("name_desc".equals(sort)) {
            sortOrder = Sort.by("name").descending();
        } else if ("price_desc".equals(sort)) {
            sortOrder = Sort.by("price").descending();
        } else {
            sortOrder = Sort.by(sort).descending();
        }
        return PageRequest.of(page, limit, sortOrder);
    }

}
