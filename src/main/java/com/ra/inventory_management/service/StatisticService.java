package com.ra.inventory_management.service;

import com.ra.inventory_management.common.Constant;
import com.ra.inventory_management.common.EOrderStatus;
import com.ra.inventory_management.model.dto.response.TotalBussinessResponse;
import com.ra.inventory_management.model.dto.response.TotalOrderStatusResponse;
import com.ra.inventory_management.model.dto.response.TotalRevenueResponse;
import com.ra.inventory_management.reponsitory.BranchRepository;
import com.ra.inventory_management.reponsitory.OrderRepository;
import com.ra.inventory_management.reponsitory.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BranchRepository branchRepository;

    public TotalBussinessResponse getTotalBussiness() {
        TotalBussinessResponse response = new TotalBussinessResponse();

        // Get total product
        Integer totalProducts = productRepository.getTotalProducts();
        response.setTotalProducts(totalProducts);

        // Get total revenue
        Double totalRevenue = orderRepository.getTotalRevenue(EOrderStatus.DONE);
        response.setTotalRevenue(totalRevenue);

        // Get total branchs
        Integer totalBranchs = branchRepository.getTotalBranchs();
        response.setTotalBranchs(totalBranchs);

        // Get low stock products
        List<Integer> lowStockProducts = productRepository.getLowStockProducts(List.of(EOrderStatus.PENDING, EOrderStatus.APPROVED));
        response.setLowStockProducts(lowStockProducts);

        return response;
    }

    public TotalOrderStatusResponse getTotalOrderStatus() {
        TotalOrderStatusResponse response = new TotalOrderStatusResponse();

        // Get total order done
        Map<EOrderStatus, Integer> totalData = orderRepository.getTotalOrderStatus(List.of(EOrderStatus.PENDING, EOrderStatus.DONE, EOrderStatus.CANCELED))
                .stream()
                .collect(Collectors.toMap(
                        obj -> obj.get("status", EOrderStatus.class),
                        obj -> obj.get("total", Number.class).intValue()
                ));

        response.setTotalOrderPending(totalData.get(EOrderStatus.PENDING));
        response.setTotalOrderDone(totalData.get(EOrderStatus.DONE));
        response.setTotalOrderCancel(totalData.get(EOrderStatus.CANCELED));

        return response;
    }

//    public List<TotalRevenueResponse> getTotalRevenue() {
//
//    }
}
