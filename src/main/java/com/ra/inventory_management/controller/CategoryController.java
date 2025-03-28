package com.ra.inventory_management.controller;

import com.ra.inventory_management.common.Constant;
import com.ra.inventory_management.model.dto.request.CategoryRequest;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.entity.Categories;
import com.ra.inventory_management.model.entity.ProductInfo;
import com.ra.inventory_management.model.entity.PurchaseOrder;
import com.ra.inventory_management.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/app/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Categories>> getCategories() {
        List<Categories> categoryList = categoryService.getAll();
        return ResponseEntity.ok(categoryList);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("searchCategories")
    public ResponseEntity<?> searchCategories(
            @RequestBody SearchRequest request
    ) {
        Page<Categories> categories = categoryService.searchCategories(request);
        return ResponseEntity.ok().body(new BaseResponse<>(categories));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/add-category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        try {
            if (categoryRequest.getActiveFlag() == null) {
                categoryRequest.setActiveFlag(1);
            }

            // Chuyển đổi CategoryRequest thành Categories entity
            Categories category = new Categories();
            category.setName(categoryRequest.getName());
            category.setCode(categoryRequest.getCode());
            category.setDescription(categoryRequest.getDescription());
            category.setActiveFlag(categoryRequest.getActiveFlag());

            // Gọi service để lưu
            Categories savedCategory = categoryService.save(category);

            // Trả về thông tin chi tiết danh mục đã thêm
            return ResponseEntity.ok(savedCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Lỗi hệ thống, vui lòng thử lại sau!"));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/activeFlag")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> requestBody) {
        Categories category = categoryService.findById(id);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy danh mục");
        }

        Integer newActiveFlag = requestBody.get("activeFlag");
        if (newActiveFlag == null) {
            return ResponseEntity.badRequest().body("Thiếu giá trị activeFlag");
        }

        category.setActiveFlag(newActiveFlag);
        categoryService.save(category);
        return ResponseEntity.ok(category);
    }

    // API lấy danh mục theo ID
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Categories category = categoryService.findById(id);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy danh mục");
        }
        return ResponseEntity.ok(category);
    }

    //  API cập nhật danh mục
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        if (categoryRequest == null) {
            return ResponseEntity.badRequest().body("Dữ liệu yêu cầu không hợp lệ!");
        }

        Categories existingCategory = categoryService.findById(id);
        if (existingCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy danh mục");
        }

        existingCategory.setName(categoryRequest.getName());
        existingCategory.setDescription(categoryRequest.getDescription());
        existingCategory.setCode(categoryRequest.getCode());
        existingCategory.setActiveFlag(categoryRequest.getActiveFlag());

        categoryService.save(existingCategory);
        return ResponseEntity.ok(existingCategory);
    }

    //  API xóa danh mục
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Categories> category = Optional.ofNullable(categoryService.findById(id));

        if (category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        }

        categoryService.delete(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

    // API import file excel
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "importExcel", produces = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> importExcel(
            @RequestParam("file") MultipartFile file
    )
            throws IOException {
        List<Categories> response = categoryService.importExcel(file);
        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }


    @GetMapping("sampleExcel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSampleExcel() throws IOException {
        Map<String, String> response = categoryService.getSampleExcel();
        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }
}