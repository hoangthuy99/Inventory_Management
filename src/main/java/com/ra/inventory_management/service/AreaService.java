package com.ra.inventory_management.service;

import com.ra.inventory_management.model.dto.request.AreaRequest;
import com.ra.inventory_management.model.entity.Area;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AreaService {
    List<Area> getAll();

    List<Area> getByBranchId(Integer branchId);

    List<Area> createOrUpdate(List<AreaRequest> requests, Long branchId);
}
