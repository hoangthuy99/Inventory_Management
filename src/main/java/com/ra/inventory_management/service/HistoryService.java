package com.ra.inventory_management.service;

import com.ra.inventory_management.model.dto.request.HistoryRequest;
import com.ra.inventory_management.model.entity.product.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HistoryService {
    Page<History> getAll(Pageable pageable, String nameSearch);
    List<History> findAll();
    History save(History history);
    History save(HistoryRequest historyRequest);
    History findById(Long id);
    void delete(Long id);
    List<History> getbyActiveFlag();
    List<History> searchByName(String keyword);
}
