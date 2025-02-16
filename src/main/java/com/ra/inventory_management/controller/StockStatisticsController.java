package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.entity.StockStatistics;
import com.ra.inventory_management.service.StockStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StockStatisticsController {

    @Autowired
    private StockStatisticsService stockStatisticsService;

}
