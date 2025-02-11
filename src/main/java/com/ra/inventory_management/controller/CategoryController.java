package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.request.CategoryRequest;
import com.ra.inventory_management.model.entity.Categories;
import com.ra.inventory_management.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/app/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<Categories>> getCategories(@RequestParam(defaultValue = "5", name = "limit") int limit, @RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "id", name = "sort") String sort, @RequestParam(defaultValue = "asc", name = "order") String order, @RequestParam(value = "nameSearch", required = false) String nameSearch) {
        Pageable pageable = order.equals("asc") ? PageRequest.of(page, limit, Sort.by(sort).ascending()) : PageRequest.of(page, limit, Sort.by(sort).descending());

        if (nameSearch != null && nameSearch.trim().isEmpty()) {
            nameSearch = null;
        }

        Page<Categories> categories = categoryService.getAll(pageable, nameSearch);

        return ResponseEntity.ok(categories);
    }

    //  add Category
    @PostMapping("/add-category")
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryRequest category) {
        try {
            if (category.getActiveFlag() == null) {
                category.setActiveFlag(1);
            }
            Categories savedCategory = categoryService.save(category);
            return ResponseEntity.ok("Category added successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

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

    // ✅ API lấy danh mục theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Categories category = categoryService.findById(id);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy danh mục");
        }
        return ResponseEntity.ok(category);
    }

    // ✅ API cập nhật danh mục
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        Categories existingCategory = categoryService.findById(id);
        if (existingCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy danh mục");
        }

        existingCategory.setName(categoryRequest.getName());
        existingCategory.setDescription(categoryRequest.getDescription());
        existingCategory.setActiveFlag(categoryRequest.getActiveFlag());

        categoryService.save(existingCategory);
        return ResponseEntity.ok(existingCategory);
    }

    // ✅ API xóa danh mục
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Categories category = categoryService.findById(id);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        }
        categoryService.delete(id);
        return ResponseEntity.ok("Category deleted successfully");
    }
}