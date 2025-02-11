package com.ra.inventory_management.controller;


import com.ra.inventory_management.model.dto.request.ProductRequest;
import com.ra.inventory_management.model.entity.ProductInfo;
import com.ra.inventory_management.service.CategoryService;
import com.ra.inventory_management.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // ✅ API lấy danh sách sản phẩm với phân trang
    @GetMapping
    public ResponseEntity<Page<ProductInfo>> getAllProducts(
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(value = "nameSearch", required = false) String nameSearch
    ) {
        Pageable pageable = productService.createPageable(page, limit, sort, order);
        Page<ProductInfo> products = productService.getAll(pageable, nameSearch);
        return ResponseEntity.ok(products);
    }

    // ✅ API thêm sản phẩm
    @PostMapping
    public ResponseEntity<ProductInfo> createProduct(@RequestBody ProductRequest productRequest) {
        ProductInfo savedProduct = productService.save(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    // ✅ API lấy sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductInfo> getProductById(@PathVariable Long id) {
        ProductInfo product = productService.findById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    // ✅ API xóa sản phẩm
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ API lấy sản phẩm theo danh mục
    @GetMapping("/category/{id}")
    public ResponseEntity<Page<ProductInfo>> getProductsByCategory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<ProductInfo> products = productService.getByCategoryId(id, pageable);
        return ResponseEntity.ok(products);
    }
}