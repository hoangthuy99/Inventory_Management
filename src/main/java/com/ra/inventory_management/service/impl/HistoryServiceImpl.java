package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.model.dto.request.HistoryRequest;
import com.ra.inventory_management.model.entity.History;
import com.ra.inventory_management.service.HistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class HistoryServiceImpl implements HistoryService {
    @Override
    public Page<History> getAll(Pageable pageable, String nameSearch) {
        return null;
    }

    @Override
    public List<History> findAll() {
        return null;
    }

    @Override
    public History save(History history) {
        return null;
    }

    @Override
    public History save(HistoryRequest historyRequest) {
        return null;
    }

    @Override
    public History findById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<History> getbyActiveFlag() {
        return null;
    }

    @Override
    public List<History> searchByName(String keyword) {
        return null;
    }
}
