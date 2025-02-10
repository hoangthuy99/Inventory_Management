package com.ra.inventory_management.Controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    //    update status
    @GetMapping("/category/status/{id}")
    public String updateStatus(@PathVariable("id") Long id) {
        Categories category = categoryService.findById(id);
        categoryService.save(category);
        return "redirect:/admin/category";
    }

    //  edit Category
    @GetMapping("/category/edit-category/{id}")
    public String edit(Model model, @PathVariable("id") Long id) {
        Categories category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "/admin/category/edit-category";
    }

    @PostMapping("/category/edit-category")
    public String update(@ModelAttribute("category") Categories category) {
        categoryService.save(category);
        return "redirect:/admin/category";
    }

    @GetMapping("/category/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return "redirect:/admin/category";
    }
}
