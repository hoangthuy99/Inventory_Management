package com.ra.inventory_management.service;

import com.ra.inventory_management.model.entity.OrderDetails;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetails> getAll(Long productId);
    OrderDetails save(OrderDetails orderDetails);
    List<OrderDetails> searchByProductName(String keyword);
    void delete(Long id);

}
