package com.ra.inventory_management.service.impl;


import com.ra.inventory_management.model.entity.Categories;
import com.ra.inventory_management.reponsitory.CategoryRepository;
import com.ra.inventory_management.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceIMPL implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;



    @Override
    public List<Categories> getAll() {
        return categoryRepository.findAll();
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
