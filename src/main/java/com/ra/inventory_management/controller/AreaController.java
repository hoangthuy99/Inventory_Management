package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.request.AreaRequest;
import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.entity.Area;
import com.ra.inventory_management.reponsitory.AreaRepository;
import com.ra.inventory_management.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("app/area")
public class AreaController {
    @Autowired
    private AreaService areaService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("list")
    public ResponseEntity<?> getAllAreas() {
        List<Area> areas = areaService.getAll();
        return ResponseEntity.ok().body(new BaseResponse<>(areas));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("getByBranch/{branchId}")
    public ResponseEntity<?> getByBranch(@PathVariable int branchId) {
        List<Area> areas = areaService.getByBranchId(branchId);
        return ResponseEntity.ok().body(new BaseResponse<>(areas));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("createOrUpdate/{branchId}")
    public ResponseEntity<?> createOrUpdate(@PathVariable Long branchId,
                                            @RequestBody List<AreaRequest> requests) {
        List<Area> areas = areaService.createOrUpdate(requests, branchId);
        return ResponseEntity.ok().body(new BaseResponse<>(areas));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete")
    public ResponseEntity<?> delete(@RequestParam List<Integer> ids) {
        Boolean deleted = areaService.deleteMulti(ids);
        return ResponseEntity.ok().body(new BaseResponse<>(deleted));
    }
}
