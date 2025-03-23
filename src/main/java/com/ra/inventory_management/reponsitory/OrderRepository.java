package com.ra.inventory_management.reponsitory;

import com.ra.inventory_management.common.EOrderStatus;
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

    @Query("SELECT o FROM Orders o WHERE o.customer.id = :customerId AND o.totalPrice = :totalPrice")
    Orders findByCustomerIdAndTotalPrice(Long customerId, BigDecimal totalPrice);

    @Query("SELECT o FROM Orders o WHERE o.customer.id = :customerId AND o.status = :status")
    List<Orders> findByCustomerIdAndStatus(Long customerId, Long orderId, Integer status);

    @Query("SELECT o from Orders o WHERE o.orderCode like ?1% ")
    List<Orders> searchByOrderCode(String keyword);

    @Query("SELECT o FROM Orders o WHERE o.deleteFg = :deleteFg")
    List<Orders> findAllActiveOrders(@Param("deleteFg") Boolean deleteFg);

    @Query("""
                select sum(o.totalPrice) from Orders o where o.status = :status
            """)
    Double getTotalRevenue(@Param("status") EOrderStatus status);

    @Query("""
                select o.status as status, sum(o.id) as total from Orders o where o.status in (:orderStatus) group by o.status
            """)
    List<Tuple> getTotalOrderStatus(@Param("orderStatus") List<EOrderStatus> orderStatus);


    @Query("""
            select o from Orders o where o.status = :status
            """)
    List<Tuple> getTotalRevenue(@Param("filterType") Integer filterType);
}
