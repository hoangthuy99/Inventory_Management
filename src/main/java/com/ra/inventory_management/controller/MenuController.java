package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.request.MenuRequest;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.entity.Menu;
import com.ra.inventory_management.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/app/menu")
@RequiredArgsConstructor
public class MenuController {

    public final MenuService menuService;
    @GetMapping("/{id}")
    public ResponseEntity<Menu> getMenuById(@PathVariable Long id) {
        Optional<Menu> menu = menuService.getByIdMenu(id);
        return menu.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Menu>> getAllMenus() {
        return ResponseEntity.ok(menuService.getAll());
    }

//    @PostMapping
//    public ResponseEntity<Menu> createMenu(@RequestBody MenuRequest menu) {
//        return ResponseEntity.ok(menuService.create(menu));
//    }

    @PutMapping("/{id}")
    public ResponseEntity<Menu> updateMenu(@RequestBody MenuRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(menuService.update(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        menuService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/search")
    public ResponseEntity<Page<Menu>> searchMenus(@RequestBody SearchRequest request) {
        Page<Menu> menus = menuService.search(request);
        return ResponseEntity.ok(menus);
    }

}
