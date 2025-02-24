package com.ra.inventory_management.service;

import com.ra.inventory_management.common.EOrderStatus;
import com.ra.inventory_management.model.entity.Customer;
import com.ra.inventory_management.model.entity.Orders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Orders> getAll(Long customId);
    Orders add(Customer customer, BigDecimal totalPrice);

    Orders save(Orders orders);
    Optional<Orders> findById(Long id);
    Orders getByIdAndStatus(Long customerId, Long orderId, EOrderStatus status);
    List<Orders> searchByOrderCode(String keyword);
}
