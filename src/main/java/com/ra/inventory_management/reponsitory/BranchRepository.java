package com.ra.inventory_management.reponsitory;

import com.ra.inventory_management.model.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    @Query("SELECT b from Branch b WHERE b.name like ?1% ")
    List<Branch> searchByName(String keyword);
}
