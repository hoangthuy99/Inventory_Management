package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.dto.response.TotalBussinessResponse;
import com.ra.inventory_management.model.dto.response.TotalOrderStatusResponse;
import com.ra.inventory_management.model.dto.response.TotalRevenueResponse;
import com.ra.inventory_management.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/statistic")
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @PreAuthorize("hasAuthority('DASHBOARD')")
    @GetMapping("getTotalBussiness")
    public ResponseEntity<?> getTotalBussiness() {
        TotalBussinessResponse response = statisticService.getTotalBussiness();

        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }

    @PreAuthorize("hasAuthority('DASHBOARD')")
    @GetMapping("getTotalOrderStatus")
    public ResponseEntity<?> getTotalOrderStatus() {
        TotalOrderStatusResponse response = statisticService.getTotalOrderStatus();

        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }

    @PreAuthorize("hasAuthority('DASHBOARD')")
    @GetMapping("getTotalRevenue/{filterType}")
    public ResponseEntity<?> getTotalRevenue(@PathVariable Integer filterType) {
        List<TotalRevenueResponse> response = statisticService.getTotalRevenue(filterType);
        return ResponseEntity.ok().body(new BaseResponse<>(response));

    }
}
