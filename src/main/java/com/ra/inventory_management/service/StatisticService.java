package com.ra.inventory_management.service;

import com.ra.inventory_management.common.Constant;
import com.ra.inventory_management.model.dto.response.TotalBussinessResponse;
import com.ra.inventory_management.model.dto.response.TotalOrderStatusResponse;
import com.ra.inventory_management.model.dto.response.TotalRevenueResponse;
import com.ra.inventory_management.reponsitory.BranchRepository;
import com.ra.inventory_management.reponsitory.OrderRepository;
import com.ra.inventory_management.reponsitory.ProductRepository;
import com.ra.inventory_management.reponsitory.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

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

    public List<TotalRevenueResponse> getTotalRevenue(Integer filterType) {
        // Get total order done
        Map<Integer, Double> revenueData = orderRepository.getTotalRevenueByFilterType(filterType, Constant.GDI_DONE)
                .stream()
                .collect(Collectors.toMap(
                        obj -> obj.get("filterType", Integer.class),
                        obj -> obj.get("totalRevenue", Double.class).doubleValue()
                ));

        // get total import cost by status done
        Map<Integer, Double> importCostData = purchaseOrderRepository.getTotalPurchaseByFilterType(filterType, Constant.GDR_DONE)
                .stream()
                .collect(Collectors.toMap(
                        obj -> obj.get("filterType", Integer.class),
                        obj -> obj.get("totalCost", Double.class).doubleValue()
                ));

        // Use Map type to group data by filter type
        Map<Integer, TotalRevenueResponse> combinedData = new HashMap<>();

        // Combine revenue data
        for (Map.Entry<Integer, Double> entry : revenueData.entrySet()) {
            combinedData.putIfAbsent(entry.getKey(), TotalRevenueResponse.builder().filterType(entry.getKey()).totalRevenue(entry.getValue()).build());
            combinedData.get(entry.getKey()).setTotalRevenue(entry.getValue());
        }

        // Combine import cost
        for (Map.Entry<Integer, Double> entry : importCostData.entrySet()) {
            combinedData.putIfAbsent(entry.getKey(), TotalRevenueResponse.builder().filterType(entry.getKey()).totalImportCost(entry.getValue()).build());
            combinedData.get(entry.getKey()).setTotalImportCost(entry.getValue());
        }

        // Change map to list
        List<TotalRevenueResponse> responses = new ArrayList<>(combinedData.values());

        // If filter type is month change integer to string
        if (filterType.equals(Constant.BY_MONTH)) {
            responses = responses.stream()
                    .map((obj -> {
                        String filter = LocalDate.of(1900, (Integer) obj.getFilterType(), 1).getMonth().toString().toLowerCase();

                        return TotalRevenueResponse.builder()
                                .filterType(filter)
                                .totalRevenue(obj.getTotalRevenue())
                                .totalImportCost(obj.getTotalImportCost())
                                .build();
                    })).toList();
        }

        return responses;
    }
}
