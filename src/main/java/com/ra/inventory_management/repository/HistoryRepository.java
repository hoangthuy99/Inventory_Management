package com.ra.inventory_management.repository;

import com.ra.inventory_management.model.entity.Categories;
import com.ra.inventory_management.model.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByActiveFlag(Integer activeFlag);
    boolean existsByName(String name);
    boolean existsById(Long id);

}
