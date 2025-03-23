package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.model.dto.request.OrderDetailRequest;
import com.ra.inventory_management.model.dto.request.OrderRequest;
import com.ra.inventory_management.model.entity.*;
import com.ra.inventory_management.reponsitory.*;
import com.ra.inventory_management.service.AuthService;
import com.ra.inventory_management.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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
    private final AuthService authService;

    public OrderServiceIMPL(AuthService authService) {
        this.authService = authService;
    }


    @Override
    public List<Orders> getAllByCus(Long customId) {
        return orderRepository.findAllByCustomerId(customId);
    }

    @Override
    public List<Orders> getAll() {
        return orderRepository.findAllActiveOrders(true);
    }

    @Override
    public Orders save(OrderRequest request) {
        Orders order = new Orders();
        order.setOrderCode(Orders.generateOrderCode());

        // Kiểm tra và lấy thông tin khách hàng
        order.setCustomer(customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại")));

        // Kiểm tra và lấy thông tin chi nhánh
        order.setBranch(branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Chi nhánh không tồn tại")));

        // Kiểm tra ngày xuất kho kế hoạch
        LocalDateTime now = LocalDateTime.now();
        if (request.getPlannedExportDate().isBefore(now)) {
            throw new RuntimeException("Ngày xuất kho kế hoạch phải từ hôm nay trở đi.");
        }
        order.setPlannedExportDate(request.getPlannedExportDate());

        // Kiểm tra ngày xuất kho thực tế (nếu có)
        if (request.getActualExportDate() != null && request.getActualExportDate().isBefore(now)) {
            throw new RuntimeException("Ngày xuất kho thực tế phải từ hôm nay trở đi.");
        }
        order.setActualExportDate(request.getActualExportDate());

        // Gán các thông tin khác
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setNote(request.getNote());
        order.setStatus(request.getStatus());
        order.setCreatedDate(LocalDateTime.now());
        order.setDeleteFg(true);

        // Tạo danh sách orderDetails
        List<OrderDetails> items = new ArrayList<>();
        for (OrderDetailRequest itemRequest : request.getOrderDetailsRequest()) {
            ProductInfo productInfo = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với id: " + itemRequest.getProductId()));

            OrderDetails orderDetails = OrderDetails.builder()
                    .order(order)
                    .createdAt(LocalDateTime.now())
                    .qty(itemRequest.getQty() != null ? itemRequest.getQty() : 0)  // Kiểm tra NULL
                    .productInfo(productInfo)
                    .productUnit(itemRequest.getProductUnit())
                    .unitPrice(productInfo.getPrice())
                    .build();

            items.add(orderDetails);
        }

        // Gán danh sách orderDetails vào order
        order.setOrderDetails(items);

        // Lưu vào database
        return orderRepository.save(order);
    }

    @Override
    public Orders update(OrderRequest orderRequest) {
        Orders order = orderRepository.findById(orderRequest.getId())
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại với ID: " + orderRequest.getId()));
        if (orderRequest.getCustomerId() != null) {
            Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
            order.setCustomer(customer);
        }

        if (orderRequest.getBranchId() != null) {
            Branch branch = branchRepository.findById(orderRequest.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Chi nhánh không tồn tại"));
            order.setBranch(branch);
        }
        if (orderRequest.getPlannedExportDate() != null) {
            if (orderRequest.getPlannedExportDate().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Ngày xuất kho kế hoạch phải từ hôm nay trở đi.");
            }
            order.setPlannedExportDate(orderRequest.getPlannedExportDate());
        }

        if (orderRequest.getActualExportDate() != null) {
            if (orderRequest.getActualExportDate().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Ngày xuất kho thực tế phải từ hôm nay trở đi.");
            }
            order.setActualExportDate(orderRequest.getActualExportDate());
        }
        if (orderRequest.getDeliveryAddress() != null) {
            order.setDeliveryAddress(orderRequest.getDeliveryAddress());
        }
        if (orderRequest.getNote() != null) {
            order.setNote(orderRequest.getNote());
        }

        if (orderRequest.getStatus() != null) {
            order.setStatus(orderRequest.getStatus());
        }
        if(orderRequest.getTotalPrice() !=null){
            order.setTotalPrice(orderRequest.getTotalPrice());
        }


        if (orderRequest.getOrderDetailsRequest() != null) {
            List<OrderDetails> updatedItems = new ArrayList<>();

            for (OrderDetailRequest itemRequest : orderRequest.getOrderDetailsRequest()) {
                ProductInfo productInfo = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với id: " + itemRequest.getProductId()));
                Optional<OrderDetails> existingOrderDetail = order.getOrderDetails().stream()
                        .filter(od ->od.getId().equals(itemRequest.getId()) )
                        .findFirst();

                if (existingOrderDetail.isPresent()) {
                    OrderDetails orderDetails = existingOrderDetail.get();
                    orderDetails.setQty(itemRequest.getQty() != null ? itemRequest.getQty() : 0);
                    orderDetails.setProductUnit(itemRequest.getProductUnit());
                    orderDetails.setUnitPrice(productInfo.getPrice());
                    orderDetails.setDeleteFg(itemRequest.getDeleteFg());
                    updatedItems.add(orderDetails);
                } else {
                    OrderDetails newOrderDetails = OrderDetails.builder()
                            .order(order)
                            .createdAt(LocalDateTime.now())
                            .qty(itemRequest.getQty() != null ? itemRequest.getQty() : 0)
                            .productInfo(productInfo)
                            .productUnit(itemRequest.getProductUnit())
                            .unitPrice(productInfo.getPrice())
                            .build();
                    updatedItems.add(newOrderDetails);
                }
            }
           order.setOrderDetails(updatedItems);
        }
        return orderRepository.save(order);
    }

    @Override
    public Optional<Orders> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Orders getByIdAndStatus(Long customerId, Long orderId, Integer status) {
        return (Orders) orderRepository.findByCustomerIdAndStatus(customerId, orderId, 1);
    }

    @Override
    public List<Orders> searchByOrderCode(String keyword) {
        return orderRepository.searchByOrderCode(keyword);
    }

    @Override
    public List<Orders> findByDeleteFg(Boolean deleteFg) {
        return orderRepository.findAllActiveOrders(true);
    }

    @Override
    public void deleteOr(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        order.setDeleteFg(false); // Đánh dấu là đã xóa
        orderRepository.save(order);
    }

}
