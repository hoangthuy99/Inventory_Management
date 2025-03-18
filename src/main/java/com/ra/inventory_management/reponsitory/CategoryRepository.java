package com.ra.inventory_management.reponsitory;

import com.ra.inventory_management.model.entity.Categories;
import com.ra.inventory_management.model.entity.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Long> {

    boolean existsByName(String name);

    boolean existsById(Long id);

    Page<Categories> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT c from Categories c WHERE c.name like ?1% ")
    List<Categories> searchCategoriesByName(String keyword);

    boolean existsByCode(String code);

    List<Categories> findByActiveFlag(int i);

    @Query("""
                SELECT c FROM Categories c
                    WHERE (:status IS NULL OR :status = -1 OR c.activeFlag = :status)
                    AND
                    (
                        :searchKey IS NULL OR :searchKey = ''
                        OR c.code LIKE %:searchKey%
                        OR c.name LIKE %:searchKey%
                    )
            """)
    Page<Categories> searchCategories(
            @Param("searchKey") String searchKey,
            @Param("status") Integer status,
            Pageable pageable
    );
}
