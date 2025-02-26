package com.ra.inventory_management.reponsitory;

import com.ra.inventory_management.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c from Customer c WHERE c.name like ?1% ")
    List<Customer> searchByName(String keyword);
}
