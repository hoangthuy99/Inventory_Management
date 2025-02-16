package com.ra.inventory_management.reponsitory;

import com.ra.inventory_management.model.entity.StockStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockStatisticsRepository extends JpaRepository<StockStatistics, Long> {


}
