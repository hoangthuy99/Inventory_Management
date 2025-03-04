package com.ra.inventory_management.service;



import com.ra.inventory_management.model.dto.request.CategoryRequest;
import com.ra.inventory_management.model.entity.Categories;


import java.util.List;

public interface CategoryService {
    List<Categories> getAll();
    Categories save(Categories category);

    Categories findById(Long id);
    void delete(Long id);
    List<Categories> getbyActiveFlag();
    List<Categories> searchByName(String keyword);
}
