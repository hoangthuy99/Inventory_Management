package com.ra.inventory_management.controller;


import com.ra.inventory_management.common.Constant;
import com.ra.inventory_management.model.dto.request.ProductRequest;
import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.dto.response.ProductResponse;
import com.ra.inventory_management.model.entity.ProductInfo;
import com.ra.inventory_management.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/product")
public class ProductController {
    @Value("${path-upload}")
    private String uploadDir;
    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductInfo> getProductById(@PathVariable Long id) {
        ProductInfo product = productService.findById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAll()
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {
        Path file = Paths.get("uploads").resolve(filename);
        Resource resource = new UrlResource(file.toUri());
        return ResponseEntity.ok().body(resource);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @PostMapping(
            value = "add-product",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> createProduct(
            @Valid @ModelAttribute ProductRequest productRequest,
            @RequestParam(value = "img", required = false) MultipartFile image) {


        // Kiểm tra xem có upload file không
        if (image != null && !image.isEmpty()) {
            try {
                // Đọc ảnh từ input stream
                BufferedImage originalImage = ImageIO.read(image.getInputStream());
                if (originalImage == null) {
                    return ResponseEntity.badRequest().body("Tệp tải lên không hợp lệ hoặc bị lỗi!");
                }
                ByteArrayOutputStream os = new ByteArrayOutputStream();

                // Lưu file vào thư mục "uploads"
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(image.getInputStream(), uploadPath.resolve(image.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu ảnh!");
            }
        }


        String imagePath = "uploads/" + image.getOriginalFilename();
        // Gọi service để lưu sản phẩm
        ProductInfo product = productService.save(productRequest, imagePath);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductRequest productRequest,
            @RequestParam(value = "img", required = false) MultipartFile image) {

        ProductInfo existingProduct = productService.findById(id);
        if (existingProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm");
        }

        String imagePath = existingProduct.getImg(); // Giữ nguyên ảnh cũ nếu không upload mới
        if (image != null && !image.isEmpty()) {
            try {
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(image.getInputStream(), uploadPath.resolve(image.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
                imagePath = "uploads/" + image.getOriginalFilename();
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu ảnh!");
            }
        }

        // Cập nhật thông tin sản phẩm
        productService.save(productRequest, imagePath);

        return ResponseEntity.ok("Cập nhật sản phẩm thành công");
    }

    //  API xóa sản phẩm
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //  API lấy sản phẩm theo danh mục
    @GetMapping("/category/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Page<ProductInfo>> getProductsByCategory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<ProductInfo> products = productService.getByCategoryId(id, pageable);
        return ResponseEntity.ok(products);
    }

    // API import file excel
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @PostMapping(value = "importExcel", produces = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> importExcel(
            @RequestParam("file") MultipartFile file
    )
            throws IOException {
        List<ProductInfo> response = productService.importExcel(file);
        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @GetMapping("sampleExcel")
    public ResponseEntity<?> getSampleExcel() throws IOException {
        Map<String, String> response = productService.getSampleExcel();
        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }
}