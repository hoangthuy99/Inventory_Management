package com.ra.inventory_management.controller;


import com.ra.inventory_management.model.dto.request.LoginRequest;
import com.ra.inventory_management.model.dto.request.RegisterRequest;
import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.dto.response.JwtResponse;
import com.ra.inventory_management.model.dto.response.RegisterResponse;
import com.ra.inventory_management.model.entity.Roles;
import com.ra.inventory_management.model.entity.UserGoogle;
import com.ra.inventory_management.model.entity.Users;
import com.ra.inventory_management.reponsitory.RoleRepository;
import com.ra.inventory_management.reponsitory.UserRepository;
import com.ra.inventory_management.service.AuthService;
import com.ra.inventory_management.service.RoleService;
import com.ra.inventory_management.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/app/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserRepository userRepository;

    private final RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        Roles roles = roleService.getById(request.getRoleId());


        // Tạo User mới
        Users newUser = new Users();
        newUser.setUserCode(request.getUserCode());
        newUser.setUsername(request.getUsername());
        newUser.setFullname(request.getFullname());
        newUser.setEmail(request.getEmail());
        newUser.setPhone(request.getPhone());
        newUser.setAddress(request.getAddress());
        newUser.setPassword(hashedPassword);
        newUser.setActiveFlag(request.getActiveFlag());
        newUser.setRoles(Set.of(roles));

        Users user = userRepository.save(newUser);
        return ResponseEntity.ok(new BaseResponse<>(user));
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyAccount(@RequestParam String code) {
        Users user = userRepository.findByUserCode(code).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("Mã xác nhận không hợp lệ!");
        }

        user.setActiveFlag(1); // Kích hoạt tài khoản
        userRepository.save(user);

        return ResponseEntity.ok("Xác nhận tài khoản thành công!");
    }

    @PostMapping("oauth-login")
    public ResponseEntity<?> oauthLogin(JwtAuthenticationToken token, HttpServletRequest request) {
        Map<String, Object> claims = token.getTokenAttributes();
        JwtResponse response = authService.oauthLogin(claims);
        response.setAccessToken(request.getHeader("Authorization").replace("Bearer ", ""));
        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }

    @PostMapping(value = "oauth-register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> oauthRegister(JwtAuthenticationToken token) {
        Map<String, Object> claims = token.getTokenAttributes();
        UserGoogle response = authService.registerOAuth(claims);
        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }

    @GetMapping("roles")
    public ResponseEntity<?> getAllRoles() {
        List<Roles> roles = roleService.getAll();
        return ResponseEntity.ok().body(new BaseResponse<>(roles));
    }

}