package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.model.dto.request.AreaRequest;
import com.ra.inventory_management.model.entity.Area;
import com.ra.inventory_management.model.entity.Branch;
import com.ra.inventory_management.reponsitory.AreaRepository;
import com.ra.inventory_management.reponsitory.BranchRepository;
import com.ra.inventory_management.service.AreaService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                    Integer id = Optional.ofNullable(data.getId()).orElse(0);
                    Area area = areaRepository.findById(id).orElse(new Area());
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

    @Override
    public Boolean deleteMulti(List<Integer> ids) {
        log.info("start: delete");

        List<Area> areas = new ArrayList<>();

        for (Integer id : ids) {
            Area area = areaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Not found area by id: " + id));

            areas.add(area);
        }
        // delete area
        areaRepository.deleteAll(areas);

        log.info("end: delete");

        return true;
    }
}
