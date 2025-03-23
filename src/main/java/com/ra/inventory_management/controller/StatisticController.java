package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.dto.response.TotalBussinessResponse;
import com.ra.inventory_management.model.dto.response.TotalOrderStatusResponse;
import com.ra.inventory_management.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/statistic")
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @GetMapping("getTotalBussiness")
    public ResponseEntity<?> getTotalBussiness() {
        TotalBussinessResponse response = statisticService.getTotalBussiness();

        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }

    @GetMapping("getTotalOrderStatus")
    public ResponseEntity<?> getTotalOrderStatus() {
        TotalOrderStatusResponse response = statisticService.getTotalOrderStatus();

        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }

}
