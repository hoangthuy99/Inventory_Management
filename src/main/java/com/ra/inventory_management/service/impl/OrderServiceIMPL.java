package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.common.EOrderStatus;
import com.ra.inventory_management.model.dto.request.OrderDetailRequest;
import com.ra.inventory_management.model.dto.request.OrderRequest;
import com.ra.inventory_management.model.entity.Branch;
import com.ra.inventory_management.model.entity.Customer;
import com.ra.inventory_management.model.entity.OrderDetails;
import com.ra.inventory_management.model.entity.Orders;
import com.ra.inventory_management.reponsitory.*;
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
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Override
    public List<Orders> getAll(Long customId) {
        return orderRepository.findAllByCustomerId(customId) ;
    }



    @Override
    public Orders save(OrderRequest orderRequest) {
        // Tạo mới một đơn hàng từ request
        Orders order = new Orders();
        // Thiết lập dữ liệu từ OrderRequest vào Orders
        order.setOrderCode(orderRequest.getOrderCode());
        order.setStatus(orderRequest.getStatus());
        order.setTotalPrice(orderRequest.getTotalPrice());
        // Lấy thông tin khách hàng từ database
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
        order.setCustomer(customer);

        // Lấy thông tin chi nhánh nếu có
        if (orderRequest.getBranchId() != null) {
            Branch branch = branchRepository.findById(orderRequest.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Chi nhánh không tồn tại"));
            order.setBranch(branch);
        }

        order.setPlannedExportDate(orderRequest.getPlannedExportDate());

            order.calculateTotalPrice(); // Gọi hàm tính tổng tiền trước khi lưu
             orderRepository.save(order);
        for (OrderDetailRequest detail : orderRequest.getOrderDetails()) {
            OrderDetails orderDetail = new OrderDetails();
            orderDetail.setOrder(order);
            orderDetail.setProductInfo(productRepository.findById(detail.getProductId()).orElseThrow());
            orderDetail.setQuantity(detail.getQuantity());
            orderDetail.setUnitPrice(detail.getUnitPrice());
            orderDetailRepository.save(orderDetail);
        }

        return order;

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
