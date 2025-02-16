package com.ra.inventory_management.service;

import com.ra.inventory_management.model.entity.StockStatistics;
import com.ra.inventory_management.reponsitory.StockStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockStatisticsService {

    @Autowired
    private StockStatisticsRepository stockStatisticsRepository;

}
