package com.ra.inventory_management.service;



import com.ra.inventory_management.model.dto.request.CategoryRequest;
import com.ra.inventory_management.model.entity.product.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    Page<Categories> getAll(Pageable pageable, String nameSearch);
    List<Categories> findAll();
    Categories save(Categories category);
    Categories save(CategoryRequest categoryRequest);
    Categories findById(Long id);
    void delete(Long id);
    List<Categories> getbyActiveFlag();
    List<Categories> searchByName(String keyword);
}
