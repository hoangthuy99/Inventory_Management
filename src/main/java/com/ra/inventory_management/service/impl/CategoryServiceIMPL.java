package com.ra.inventory_management.service.impl;


import com.ra.inventory_management.model.dto.request.CategoryRequest;
import com.ra.inventory_management.model.entity.product.Categories;
import com.ra.inventory_management.reponsitory.CategoryRepository;
import com.ra.inventory_management.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceIMPL implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Page<Categories> getAll(Pageable pageable, String nameSearch) {
        if (nameSearch != null) return categoryRepository.findAllByNameContainingIgnoreCase(nameSearch, pageable);
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Categories> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Categories save(Categories category) {
        return categoryRepository.save(category);
    }

    @Override
    public Categories save(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new IllegalArgumentException("Tên danh mục đã tồn tại, vui lòng nhập tên danh mục khác!");
        }
        if (categoryRepository.existsByCode(categoryRequest.getCode())) {
            throw new IllegalArgumentException("Mã code danh mục đã tồn tại, vui lòng nhập code danh mục khác!");
        }
        Categories category = new Categories();
        category.setName(categoryRequest.getName());
        category.setCode(categoryRequest.getCode());
        category.setDescription(categoryRequest.getDescription());
        category.setActiveFlag(categoryRequest.getActiveFlag() != null ? categoryRequest.getActiveFlag() : 1);

        return categoryRepository.save(category);
    }

    @Override
    public Categories findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy danh mục");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Categories> getbyActiveFlag() {
        return categoryRepository.findByActiveFlag(1);
    }


    @Override
    public List<Categories> searchByName(String keyword) {
        return categoryRepository.searchCategoriesByName(keyword);
    }
}
