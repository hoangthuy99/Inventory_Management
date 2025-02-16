package com.ra.inventory_management.controller;


import com.ra.inventory_management.model.dto.request.ProductRequest;
import com.ra.inventory_management.model.entity.product.ProductInfo;
import com.ra.inventory_management.service.CategoryService;
import com.ra.inventory_management.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

import net.coobird.thumbnailator.Thumbnails;

@RestController
@RequestMapping("/app/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    //  API lấy danh sách sản phẩm với phân trang
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

    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @ModelAttribute ProductRequest request,
            @RequestParam(value = "img", required = false) MultipartFile image
    ) {
        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            try {
                // Kiểm tra định dạng file
                String fileType = image.getContentType();
                if (!fileType.equals("image/jpeg") && !fileType.equals("image/png") && !fileType.equals("image/jpg")) {
                    return ResponseEntity.badRequest().body("Chỉ chấp nhận định dạng JPG, JPEG, PNG!");
                }

                // Tạo tên file mới để tránh trùng lặp
                String fileExtension = fileType.split("/")[1];
                String fileName = System.currentTimeMillis() + "." + fileExtension;

                // Định dạng ảnh (resize + nén)
                BufferedImage originalImage = ImageIO.read(image.getInputStream());
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                Thumbnails.of(originalImage)
                        .size(300, 300)  // Resize ảnh về 300x300 px
                        .outputQuality(0.7)  // Nén ảnh (70% chất lượng)
                        .toOutputStream(os);

                // Lưu file vào thư mục "uploads"
                Path uploadPath = Paths.get("uploads/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.write(uploadPath.resolve(fileName), os.toByteArray());

                imagePath = "/uploads/" + fileName;
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu ảnh!");
            }
        }

        // Gán đường dẫn ảnh vào product request
        request.setImg(imagePath);

        // Lưu sản phẩm bằng service
        ProductInfo savedProduct = productService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }
    // API lấy sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductInfo> getProductById(@PathVariable Long id) {
        ProductInfo product = productService.findById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    //  API xóa sản phẩm
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //  API lấy sản phẩm theo danh mục
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