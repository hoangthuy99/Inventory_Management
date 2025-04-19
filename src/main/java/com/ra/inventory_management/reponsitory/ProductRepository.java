package com.ra.inventory_management.reponsitory;

import com.ra.inventory_management.model.entity.Categories;
import com.ra.inventory_management.model.entity.ProductInfo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductInfo, Long> {
    @Query("SELECT pro from ProductInfo pro WHERE pro.name like %?1% ")
    List<ProductInfo> findByNameOrDescription(String name, String description);

    @Query("SELECT p from ProductInfo p WHERE p.name like %?1% ")
    List<ProductInfo> searchProductByName(String keyword);

    @Query("SELECT p FROM ProductInfo p WHERE p.categories.activeFlag = :activeFlag")
    Page<ProductInfo> findByCategoriesAndActiveFlag(Pageable pageable, @Param("activeFlag") Integer activeFlag);

    @Query("select p from ProductInfo p where p.categories.id = :id")
    Page<ProductInfo> findByCategoryId(Long id, Pageable pageable);


    Page<ProductInfo> findByCategoriesId(Long id, Pageable pageable);


    boolean existsByName(String name);

    int countByCategories(Categories category);

    @Query("""
                select count(p.id) from ProductInfo p
            """)
    Integer getTotalProducts();

    @Query("""
            select p.id from ProductInfo p 
            where p.id = (
            select od.productInfo.id from OrderDetails od where od.qty > p.qty and od.order.status in (:orderStatus)
            )
            """)
    List<Integer> getLowStockProducts(@Param("orderStatus") List<Integer> orderStatus);

    Optional<ProductInfo> findByName(String name);
}
