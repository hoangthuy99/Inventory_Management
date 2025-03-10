package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.model.dto.request.AreaRequest;
import com.ra.inventory_management.model.entity.Area;
import com.ra.inventory_management.model.entity.Branch;
import com.ra.inventory_management.reponsitory.AreaRepository;
import com.ra.inventory_management.reponsitory.BranchRepository;
import com.ra.inventory_management.service.AreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AreaServiceIMPL implements AreaService {
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private BranchRepository branchRepository;

    @Override
    public List<Area> getAll() {
        log.info("start: getAll");

        List<Area> areas = areaRepository.findAll();

        log.info("end: getAll");
        return areas;
    }

    @Override
    public List<Area> getByBranchId(Integer branchId) {
        log.info("start: getByBranchId");

        List<Area> areas = areaRepository.findByBranchId(branchId);

        log.info("end: getByBranchId");
        return areas;
    }


    @Override
    @Transactional
    public List<Area> createOrUpdate(List<AreaRequest> requests, Long branchId) {
        log.info("start: createOrUpdate");

        // find branch by id if not found throw error
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Not found branch by id: " + branchId));

        // prepare data
        List<Area> areas = requests.stream()
                .map(data -> {
                    Area area = new Area();
                    area.setId(data.getId());
                    area.setName(data.getName());
                    area.setCapacity(data.getCapacity());
                    area.setBranch(branch);
                    area.setPosX(data.getPosX());
                    area.setPosY(data.getPosY());
                    area.setDescription(data.getDescription());
                    area.setActiveFlag(1);
                    return area;
                }).toList();

        List<Area> storedAreas = areaRepository.saveAll(areas);

        log.info("end: createOrUpdate");

        return storedAreas;
    }
}
