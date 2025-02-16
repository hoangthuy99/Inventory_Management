package com.ra.inventory_management.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.ra.inventory_management.service.ProductInStockService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/app/history")
public class HistoryController {

//    @Autowired
//    private ProductService productService;
//
//    @Autowired
//    private ProductInStockService productInStockService;
//
//    // Lấy danh sách tất cả sản phẩm
//    @GetMapping("/products")
//    public ResponseEntity<List<ProductInfo>> getAllProducts() {
//        return ResponseEntity.ok(productInfoService.getAllProducts());
//    }
//
//    // Lấy chi tiết sản phẩm theo ID
//    @GetMapping("/products/{id}")
//    public ResponseEntity<ProductInfo> getProductById(@PathVariable Long id) {
//        return ResponseEntity.ok(productInfoService.getProductById(id));
//    }
//
//    // Lấy danh sách tồn kho
//    @GetMapping("/stocks")
//    public ResponseEntity<List<ProductInStock>> getAllStock() {
//        return ResponseEntity.ok(productInStockService.getAllStock());
//    }
//
//    // Lấy chi tiết tồn kho theo ID
//    @GetMapping("/stocks/{id}")
//    public ResponseEntity<ProductInStock> getStockById(@PathVariable Long id) {
//        return ResponseEntity.ok(productInStockService.getStockById(id));
//    }
}
