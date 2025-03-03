package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.common.EOrderStatus;
import com.ra.inventory_management.model.entity.Customer;
import com.ra.inventory_management.model.entity.Orders;
import com.ra.inventory_management.reponsitory.OrderRepository;
import com.ra.inventory_management.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceIMPL implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Override
    public List<Orders> getAll(Long customId) {
        return orderRepository.findAllByCustomerId(customId) ;
    }

    @Override
    public Orders add(Customer customer, BigDecimal totalPrice) {
       return orderRepository.findByCustomerIdAndTotalPrice(customer.getId(), totalPrice);
    }

    @Override
    public Orders save(Orders orders) {
        Orders order = new Orders();
        order.setOrderCode(order.getOrderCode());
        order.setStatus(order.getStatus());
        order.setTotalPrice(order.getTotalPrice());
        order.setCustomer(order.getCustomer());
        return orderRepository.save(order);
    }

    @Override
    public Optional<Orders> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Orders getByIdAndStatus(Long customerId, Long orderId, EOrderStatus status) {
        return (Orders) orderRepository.findByCustomerIdAndStatus(customerId,orderId,1);
    }

    @Override
    public List<Orders> searchByOrderCode(String keyword) {
        return orderRepository.searchByOrderCode(keyword);
    }
}
