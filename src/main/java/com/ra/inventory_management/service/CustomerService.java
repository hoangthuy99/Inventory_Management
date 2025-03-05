package com.ra.inventory_management.service;

import com.ra.inventory_management.model.entity.Customer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomerService {
    List<Customer> getAll();

    Customer save(Customer customer);

    Optional<Customer> findById(Long id);

    void delete(Long id);

    List<Customer> searchByName(String keyword);

    List<Customer> importExcel(MultipartFile file) throws IOException;

    Map<String, String> getSampleExcel() throws IOException;
}
