package com.ra.inventory_management.reponsitory;

import com.ra.inventory_management.model.entity.Orders;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o WHERE o.customer.id = :customerId")
    List<Orders> findAllByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT od.qty FROM OrderDetails od WHERE od.order.id = :orderId")
    List<Integer> findQtyByOrderId(@Param("orderId") Long orderId);
    @Query("SELECT o FROM Orders o LEFT JOIN FETCH o.orderDetails WHERE o.deleteFg = :deleteFg")
    List<Orders> findAllActiveOrders(@Param("deleteFg") Boolean deleteFg);

    @Query("SELECT o FROM Orders o WHERE o.customer.id = :customerId AND o.status = :status")
    List<Orders> findByCustomerIdAndStatus(Long customerId, Long orderId, Integer status);

    @Query("SELECT o from Orders o WHERE o.orderCode like ?1% ")
    List<Orders> searchByOrderCode(String keyword);



    @Query("""
                select sum(o.totalPrice) from Orders o where o.status = :status
            """)
    Double getTotalRevenue(@Param("status") Integer status);

    @Query("""
                select o.status as status, count(distinct o.id) as total from Orders o where o.status in (:orderStatus) group by o.status
            """)
    List<Tuple> getTotalOrderStatus(@Param("orderStatus") List<Integer> orderStatus);

    @Query("""
            select 
            (
                case when :filterType = 1 then month(o.actualExportDate)
                when :filterType = 2 then quarter(o.actualExportDate) 
                when :filterType = 3 then year(o.actualExportDate)
                end
            ) as filterType, 
            cast(sum(o.totalPrice) as double ) as totalRevenue 
            from Orders o where o.status = :orderStatus
            group by (
                case when :filterType = 1 then month(o.actualExportDate)
                when :filterType = 2 then quarter(o.actualExportDate) 
                when :filterType = 3 then year(o.actualExportDate)
                end
            )
            """)
    List<Tuple> getTotalRevenueByFilterType(@Param("filterType") Integer filterType, @Param("orderStatus") Integer orderStatus);
}
