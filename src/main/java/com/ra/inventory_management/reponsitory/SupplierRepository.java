package com.ra.inventory_management.reponsitory;


import com.ra.inventory_management.model.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    @Query("SELECT s from Supplier s WHERE s.name like ?1% ")
    List<Supplier> searchByName(String keyword);
}
