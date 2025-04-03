package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.request.RegisterRequest;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.entity.Users;
import com.ra.inventory_management.service.RoleService;
import com.ra.inventory_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping("search")
    public ResponseEntity<?> userPage(@RequestBody SearchRequest request) {
        Page<Users> users = userService.search(request);
        return ResponseEntity.ok().body(new BaseResponse<>(users));
    }
    @GetMapping()
    public ResponseEntity<List<Users>> getAll(){
         List<Users> users = userService.getAll();
         return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Users users = userService.findById(id);
        return ResponseEntity.ok().body(new BaseResponse<>(users));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RegisterRequest request) {
        Users users = userService.update(request, id);
        return ResponseEntity.ok().body(new BaseResponse<>(users));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok().body(new BaseResponse<>(true));
    }
}
