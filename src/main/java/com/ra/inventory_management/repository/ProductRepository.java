package com.ra.inventory_management.repository;


import com.ra.inventory_management.model.entity.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.ra.inventory_management.model.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProductRepository extends JpaRepository<ProductInfo, Long> {
    @Query("SELECT pro from ProductInfo pro WHERE pro.name like %?1% ")
    List<ProductInfo> findByNameOrDescription(String name, String description);
    @Query("SELECT p from ProductInfo p WHERE p.name like %?1% ")
    List<ProductInfo> searchProductByName (String keyword);
    @Query("select p from ProductInfo p where p.categories.activeFlag = :activeFlag")
    Page<ProductInfo> findByCategoriesAndActiveFlag(Pageable pageable,Integer activeFlag);
    @Query("select p from ProductInfo p where p.categories.id = :id")
    Page<ProductInfo> findByCategoryId(Long id, Pageable pageable);



    boolean existsByName(String name);


}
