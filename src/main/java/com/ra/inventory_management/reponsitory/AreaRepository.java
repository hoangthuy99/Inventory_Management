package com.ra.inventory_management.reponsitory;

import com.ra.inventory_management.model.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {
    List<Area> findByBranchId(Integer branchId);
}
