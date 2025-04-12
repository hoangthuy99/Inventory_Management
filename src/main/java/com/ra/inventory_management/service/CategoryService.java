package com.ra.inventory_management.service;


import com.ra.inventory_management.model.dto.request.CategoryRequest;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.entity.Categories;
import com.ra.inventory_management.model.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<Categories> getAll();

    Categories save(Categories category);

    Categories findById(Long id);

    void delete(Long id);


    List<Categories> searchByName(String keyword);

    List<Categories> importExcel(MultipartFile file) throws IOException;

    Map<String, String> getSampleExcel() throws IOException;

    Page<Categories> searchCategories(SearchRequest request);
}
