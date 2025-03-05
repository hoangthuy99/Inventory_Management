package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.common.Constant;
import com.ra.inventory_management.model.entity.Categories;
import com.ra.inventory_management.model.entity.Customer;
import com.ra.inventory_management.reponsitory.CustomerRepository;
import com.ra.inventory_management.service.CustomerService;
import com.ra.inventory_management.util.ExcelUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceIMPL implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ExcelUtil excelUtil;

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public List<Customer> searchByName(String keyword) {
        return customerRepository.searchByName(keyword);
    }

    @Override
    public List<Customer> importExcel(MultipartFile file) throws IOException {
        if (!excelUtil.hasExcelFormat(file)) {
            throw new RuntimeException("Excel file wrong format");
        }
        try {
            Iterator rows = excelUtil.toIterator(file.getInputStream());
            List<Customer> customerList = new ArrayList<>();

            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = (Row) rows.next();

                Iterator<Cell> cellsInRow = currentRow.iterator();
                int cellIndex = 0;
                Customer customer = new Customer();

                while (cellsInRow.hasNext()) {

                    Cell currCell = cellsInRow.next();
                    switch (cellIndex) {
                        case 0:
                            customer.setCusCode(currCell.getStringCellValue());
                            break;
                        case 1:
                            customer.setName(currCell.getStringCellValue());
                            break;
                        case 2:
                            customer.setEmail(currCell.getStringCellValue());
                            break;
                        case 3:
                            customer.setPhone(String.valueOf(currCell.getNumericCellValue()));
                            break;
                        case 4:
                            customer.setAddress(currCell.getStringCellValue());
                            break;
                    }

                    customer.setActiveFlag(1);
                    customer.setCreatedDate(LocalDateTime.now());

                    cellIndex++;
                }

                customerList.add(customer);
            }

            return customerRepository.saveAll(customerList);
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
            ClassPathResource pathResource = new ClassPathResource(Constant.CUSTOMER_SAMPLE);
            if (!pathResource.exists()) {
                throw new FileNotFoundException("Không tìm thấy file: " + pathResource.getPath());
            }

            // Copy file to temp folder
            Path tempFile = Files.createTempFile("sample_customer", ".xlsx");
            Files.copy(pathResource.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            // Convert base64
            byte[] fileContent = Files.readAllBytes(tempFile);
            String base64Encoded = Base64.encodeBase64String(fileContent);


            // Remove file temp after write
            Files.deleteIfExists(tempFile);

            Map<String, String> response = new HashMap<>();
            response.put("fileName", "sample_customer");
            response.put("base64", "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64," + base64Encoded);

            return response;
        } catch (IOException e) {
            throw e;
        }
    }
}
