package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.model.entity.Customer;
import com.ra.inventory_management.reponsitory.CustomerRepository;
import com.ra.inventory_management.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceIMPL implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
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
}
