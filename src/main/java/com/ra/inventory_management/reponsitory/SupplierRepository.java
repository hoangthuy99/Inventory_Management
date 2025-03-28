package com.ra.inventory_management.reponsitory;


import com.ra.inventory_management.model.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    @Query("""
                SELECT s FROM Supplier s
                    WHERE (:status IS NULL OR :status = -1 OR s.activeFlag = :status)
                    AND
                    (
                        :searchKey IS NULL OR :searchKey = ''
                        OR s.subCode LIKE %:searchKey%
                        OR s.name LIKE %:searchKey%
                    )
            """)
    Page<Supplier> searchSupplier(
            @Param("searchKey") String searchKey,
            @Param("status") Integer status,
            Pageable pageable
    );
}
