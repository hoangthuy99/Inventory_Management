package com.ra.inventory_management.repository;

import com.ra.inventory_management.model.entity.Categories;
import com.ra.inventory_management.model.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o WHERE o.customer.id = :customerId")
    List<Orders> findAllByCustomerId(Long customerId);

    @Query("SELECT o FROM Orders o WHERE o.customer.id = :customerId AND o.totalPrice = :totalPrice")
    Orders findByCustomerIdAndTotalPrice(Long customerId, BigDecimal totalPrice);

    @Query("SELECT o FROM Orders o WHERE o.customer.id = :customerId AND o.status = :status")
    List<Orders> findByCustomerIdAndStatus(Long customerId, Long orderId, Integer status);

    @Query("SELECT o from Orders o WHERE o.orderCode like ?1% ")
    List<Orders> searchByOrderCode(String keyword);
}
