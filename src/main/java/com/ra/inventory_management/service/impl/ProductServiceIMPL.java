package com.ra.inventory_management.service.impl;


import com.ra.inventory_management.common.Constant;
import com.ra.inventory_management.model.dto.request.ProductRequest;
import com.ra.inventory_management.model.entity.Categories;
import com.ra.inventory_management.model.entity.ProductInfo;
import com.ra.inventory_management.reponsitory.CategoryRepository;
import com.ra.inventory_management.reponsitory.ProductRepository;
import com.ra.inventory_management.sercurity.exception.ResourceNotFoundException;
import com.ra.inventory_management.service.ProductService;
import com.ra.inventory_management.util.ExcelUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class ProductServiceIMPL implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ExcelUtil excelUtil;

    @Value("${path-upload}")
    private String basePath;

    @Override
    public List<ProductInfo> getAll() {
        return productRepository.findAll();
    }

    @Override
    public ProductInfo save(ProductRequest productRequest, String imagePath, Long id) {
        try {
            if (id == null) {
                // Tạo mới: kiểm tra tên tồn tại
                if (productRepository.existsByName(productRequest.getName())) {
                    throw new IllegalArgumentException("Tên sản phẩm đã tồn tại, vui lòng nhập tên sản phẩm khác!");
                }
            } else {
                // Cập nhật: kiểm tra trùng tên với sản phẩm khác
                Optional<ProductInfo> existingByName = productRepository.findByName(productRequest.getName());
                if (existingByName.isPresent() && !existingByName.get().getId().equals(id)) {
                    throw new IllegalArgumentException("Tên sản phẩm đã tồn tại, vui lòng nhập tên sản phẩm khác!");
                }
            }

            if (productRequest.getCategoryId() == null) {
                throw new IllegalArgumentException("ID danh mục không được để trống");
            }

            Categories category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("ID danh mục không hợp lệ"));

            ProductInfo product;
            if (id == null) {
                product = new ProductInfo();
                String generatedCode = ProductInfo.generateOrderCode();
                product.setCode(generatedCode);
            } else {
                product = productRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm để cập nhật"));
            }

            product.setName(productRequest.getName());
            product.setQty(productRequest.getQty());
            product.setPrice(BigDecimal.valueOf(productRequest.getPrice()));
            product.setDescription(productRequest.getDescription());
            product.setActiveFlag(productRequest.getActiveFlag() != null ? productRequest.getActiveFlag() : 1);
            product.setCategories(category);
            product.setImg(imagePath);

            return productRepository.save(product);

        } catch (IllegalArgumentException e) {
            logger.error("Validation error: ", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Lỗi khi lưu sản phẩm", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống, vui lòng thử lại!", e);
        }
    }

    @Override
    public ProductInfo findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<ProductInfo> getByCategoryActiveFlag(Pageable pageable, Integer activeFlag) {
        return productRepository.findByCategoriesAndActiveFlag(pageable, activeFlag);
    }

    @Override
    public Page<ProductInfo> getByCategoryId(Long id, Pageable pageable) {
        return productRepository.findByCategoryId(id, pageable);
    }


    @Override
    public List<ProductInfo> searchByName(String keyword) {
        return productRepository.searchProductByName(keyword);
    }


    @Override
    public List<ProductInfo> importExcel(MultipartFile file) throws IOException {
        if (!excelUtil.hasExcelFormat(file)) {
            throw new RuntimeException("Excel file wrong format");
        }
        try {
            Iterator rows = excelUtil.toIterator(file.getInputStream());
            List<ProductInfo> productInfos = new ArrayList<>();

            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = (Row) rows.next();

                Iterator<Cell> cellsInRow = currentRow.iterator();
                int cellIndex = 0;
                ProductInfo productInfo = new ProductInfo();

                while (cellsInRow.hasNext()) {

                    Cell currCell = cellsInRow.next();
                    switch (cellIndex) {
                        case 0:
                            productInfo.setName(currCell.getStringCellValue());
                            break;
                        case 1:
                            productInfo.setCode(currCell.getStringCellValue());
                            break;
                        case 2:
                            productInfo.setQty((int) currCell.getNumericCellValue());
                            break;
                        case 3:
                            productInfo.setPrice(BigDecimal.valueOf(currCell.getNumericCellValue()));
                            break;
                        case 4:
                            productInfo.setDescription(currCell.getStringCellValue());
                            break;
                        case 5:
                            Categories categories = categoryRepository.findById(
                                    (long) currCell.getNumericCellValue()
                            ).orElseThrow(() -> new ResourceNotFoundException("Not found category by id: " + (long) currCell.getNumericCellValue()));
                            productInfo.setCategories(categories);
                            break;
                    }

                    productInfo.setActiveFlag(1);
                    productInfo.setCreatedDate(LocalDateTime.now());

                    cellIndex++;
                }

                productInfos.add(productInfo);
            }

            return productRepository.saveAll(productInfos);
        } catch (IOException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Dữ liệu bị trùng lặp");
        }
    }

    @Override
    public Map<String, String> getSampleExcel() throws IOException {
        try {
            // Get file from resource
            ClassPathResource pathResource = new ClassPathResource(Constant.PRODUCT_SAMPLE);
            if (!pathResource.exists()) {
                throw new FileNotFoundException("Không tìm thấy file: " + pathResource.getPath());
            }

            // Copy file to temp folder
            Path tempFile = Files.createTempFile("sample_product", ".xlsx");
            Files.copy(pathResource.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
            FileInputStream inputStream = new FileInputStream(tempFile.toFile());

            // Create workbook
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(1);
            inputStream.close();

            // Get all category
            List<Categories> categories = categoryRepository.findAll();

            // Write data in file
            int rowNum = sheet.getLastRowNum() + 1;
            for (Categories item : categories) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getId());
                row.createCell(1).setCellValue(item.getName());
                row.createCell(2).setCellValue(item.getCode());
                row.createCell(3).setCellValue(item.getDescription());
            }

            // Write out
            FileOutputStream outputStream = new FileOutputStream(tempFile.toFile());
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();

            // Convert base64
            byte[] fileContent = Files.readAllBytes(tempFile);
            String base64Encoded = Base64.encodeBase64String(fileContent);


            // Remove file temp after write
            Files.deleteIfExists(tempFile);

            Map<String, String> response = new HashMap<>();
            response.put("fileName", "sample_product");
            response.put("base64", "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64," + base64Encoded);

            return response;

        } catch (IOException e) {
            throw e;
        }
    }
}
