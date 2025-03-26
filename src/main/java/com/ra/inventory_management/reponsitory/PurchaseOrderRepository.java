package com.ra.inventory_management.reponsitory;


import com.ra.inventory_management.model.entity.PurchaseOrder;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Query("""
            select 
            (
                case when :filterType = 1 then month(p.orderDate)
                when :filterType = 2 then quarter(p.orderDate) 
                when :filterType = 3 then year(p.orderDate)
                end
            ) as filterType, 
            cast(sum(p.totalAmount) as double ) as totalCost 
            from PurchaseOrder p where p.status = :purchaseStatus
            group by (
                case when :filterType = 1 then month(p.orderDate)
                when :filterType = 2 then quarter(p.orderDate) 
                when :filterType = 3 then year(p.orderDate)
                end
            )
            """)
    List<Tuple> getTotalPurchaseByFilterType(@Param("filterType") Integer filterType, @Param("purchaseStatus") Integer orderStatus);

}
