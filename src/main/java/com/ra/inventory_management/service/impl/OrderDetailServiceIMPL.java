package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.model.entity.OrderDetails;
import com.ra.inventory_management.reponsitory.OrderDetailRepository;
import com.ra.inventory_management.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderDetailServiceIMPL implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Override
    public List<OrderDetails> getAll(Long productId) {
        return orderDetailRepository.findByProductInfoId(productId);
    }

    @Override
    public OrderDetails save(OrderDetails orderDetails) {
        return orderDetailRepository.save(orderDetails);
    }


    @Override
    public List<OrderDetails> searchByProductName(String keyword) {
        return orderDetailRepository.searchByProductName(keyword);
    }

    @Override
    public void delete(Long id) {
        if (!orderDetailRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy OrderDetail");
        }
        orderDetailRepository.deleteById(id);
    }
}
