package com.ra.inventory_management.service.impl;



import com.ra.inventory_management.model.dto.request.ProductInStockRequest;
import com.ra.inventory_management.model.dto.request.ProductRequest;
import com.ra.inventory_management.model.entity.product.Categories;
import com.ra.inventory_management.model.entity.product.ProductInfo;
import com.ra.inventory_management.reponsitory.CategoryRepository;
import com.ra.inventory_management.reponsitory.ProductRepository;
import com.ra.inventory_management.service.ProductService;
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
import java.util.Optional;

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
        try {
            if (productRepository.existsByName(productRequest.getName())) {
                throw new IllegalArgumentException("Tên sản phẩm đã tồn tại, vui lòng nhập tên sản phẩm khác!");
            }

            // Kiểm tra ID danh mục
            if (productRequest.getCategoryId() == null) {
                throw new IllegalArgumentException("ID danh mục không được để trống");
            }

            Categories category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("ID danh mục không hợp lệ"));

            // Tạo mới đối tượng Product
            ProductInfo product = new ProductInfo();
            product.setName(productRequest.getName());
            product.setCode(productRequest.getCode());
            product.setQty(productRequest.getQty());

            if (productRequest.getPrice() == null) {
                throw new IllegalArgumentException("Giá sản phẩm không được để trống");
            }
            product.setPrice(BigDecimal.valueOf(productRequest.getPrice()));

            product.setDescription(productRequest.getDescription());
            product.setImg(productRequest.getImg());  // Ảnh đã được cập nhật từ Controller
            product.setActiveFlag(productRequest.getActiveFlag() != null ? productRequest.getActiveFlag() : 1);
            product.setCategories(category);

            return productRepository.save(product);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
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
