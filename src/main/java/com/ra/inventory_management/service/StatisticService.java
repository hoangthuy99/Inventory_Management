package com.ra.inventory_management.service;

import com.ra.inventory_management.common.Constant;
import com.ra.inventory_management.model.dto.response.TotalBussinessResponse;
import com.ra.inventory_management.model.dto.response.TotalOrderStatusResponse;
import com.ra.inventory_management.model.dto.response.TotalRevenueResponse;
import com.ra.inventory_management.reponsitory.BranchRepository;
import com.ra.inventory_management.reponsitory.OrderRepository;
import com.ra.inventory_management.reponsitory.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        Double totalRevenue = orderRepository.getTotalRevenue(Constant.GDI_DONE);
        response.setTotalRevenue(totalRevenue);

        // Get total branchs
        Integer totalBranchs = branchRepository.getTotalBranchs();
        response.setTotalBranchs(totalBranchs);

        // Get low stock products
        List<Integer> lowStockProducts = productRepository.getLowStockProducts(List.of(Constant.GDI_PENDING, Constant.GDI_APPROVED));
        response.setLowStockProducts(lowStockProducts);

        return response;
    }

    public TotalOrderStatusResponse getTotalOrderStatus() {
        TotalOrderStatusResponse response = new TotalOrderStatusResponse();

        // Get total order done
        Map<Integer, Integer> totalData = orderRepository.getTotalOrderStatus(List.of(Constant.GDI_PENDING, Constant.GDI_DONE, Constant.GDI_CANCELED))
                .stream()
                .collect(Collectors.toMap(
                        obj -> obj.get("status", Number.class).intValue(),
                        obj -> obj.get("total", Number.class).intValue()
                ));

        response.setTotalOrderPending(totalData.get(Constant.GDI_PENDING));
        response.setTotalOrderDone(totalData.get(Constant.GDI_DONE));
        response.setTotalOrderCancel(totalData.get(Constant.GDI_CANCELED));

        return response;
    }

    public List<TotalRevenueResponse> getTotalRevenue() {
        // Get total order done
        List<TotalRevenueResponse> totalData = orderRepository.getTotalRevenueByFilterType(Constant.BY_YEAR, Constant.GDI_DONE)
                .stream()
                .map(obj -> TotalRevenueResponse.builder().filterType(obj.get("filterType", Integer.class))
                        .totalRevenue(obj.get("total", Double.class).doubleValue()).build()).toList();


        return totalData;
    }
}
