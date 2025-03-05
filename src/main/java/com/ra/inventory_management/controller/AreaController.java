package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.entity.Area;
import com.ra.inventory_management.reponsitory.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("app/area")
public class AreaController {
    @Autowired
    private AreaRepository areaRepository;

    @GetMapping("list")
    public ResponseEntity<?> getAllAreas(){
        List<Area> areas = areaRepository.findAll();
        return ResponseEntity.ok().body(new BaseResponse<>(areas));
    }
}
