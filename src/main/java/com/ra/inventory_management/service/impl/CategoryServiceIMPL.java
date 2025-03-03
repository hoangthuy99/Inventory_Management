package com.ra.inventory_management.service.impl;


import com.ra.inventory_management.model.entity.Categories;
import com.ra.inventory_management.model.entity.ProductInfo;
import com.ra.inventory_management.reponsitory.CategoryRepository;
import com.ra.inventory_management.sercurity.exception.ResourceNotFoundException;
import com.ra.inventory_management.service.CategoryService;
import com.ra.inventory_management.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CategoryServiceIMPL implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ExcelUtil excelUtil;


    @Override
    public List<Categories> getAll() {
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

    @Override
    public List<Categories> importExcel(MultipartFile file) throws IOException {
        if (!excelUtil.hasExcelFormat(file)) {
            throw new RuntimeException("Excel file wrong format");
        }
        try {
            Iterator rows = excelUtil.toIterator(file.getInputStream());
            List<Categories> categoriesList = new ArrayList<>();

            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = (Row) rows.next();

                Iterator<Cell> cellsInRow = currentRow.iterator();
                int cellIndex = 0;
                Categories categories = new Categories();

                while (cellsInRow.hasNext()) {

                    Cell currCell = cellsInRow.next();
                    switch (cellIndex) {
                        case 0:
                            categories.setName(currCell.getStringCellValue());
                            break;
                        case 1:
                            categories.setCode(currCell.getStringCellValue());
                            break;
                        case 2:
                            categories.setDescription(currCell.getStringCellValue());
                            break;
                    }

                    categories.setActiveFlag(1);
                    categories.setCreatedDate(LocalDateTime.now());

                    cellIndex++;
                }

                categoriesList.add(categories);
            }

            return categoryRepository.saveAll(categoriesList);
        } catch (IOException e) {
            throw e;
        }
    }
}
