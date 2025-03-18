package com.ra.inventory_management.reponsitory;


import com.ra.inventory_management.model.entity.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {

    @Query("""
                SELECT p FROM PurchaseOrder p
                    WHERE p.deleteFg = false
                    AND (
                        :searchKey IS NULL OR :searchKey = ''
                        OR p.code LIKE %:searchKey%
                        OR p.supplier.name LIKE %:searchKey%
                        OR p.branch.name LIKE %:searchKey%
                    )
                    AND (:status IS NULL OR :status = -1 OR p.status = :status)
            """)
    Page<PurchaseOrder> searchPurchase(
            @Param("searchKey") String searchKey,
            @Param("status") Integer status,
            Pageable pageable
    );
}
