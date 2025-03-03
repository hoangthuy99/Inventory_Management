package com.ra.inventory_management.service;

import com.ra.inventory_management.model.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> getAll();
    Customer save(Customer customer);

    Optional<Customer> findById(Long id);
    void delete(Long id);
    List<Customer> searchByName(String keyword);
}
