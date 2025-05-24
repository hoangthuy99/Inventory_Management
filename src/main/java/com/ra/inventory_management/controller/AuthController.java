package com.ra.inventory_management.controller;


import com.ra.inventory_management.common.ERoles;
import com.ra.inventory_management.model.dto.request.LoginRequest;
import com.ra.inventory_management.model.dto.request.RegisterRequest;
import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.dto.response.JwtResponse;
import com.ra.inventory_management.model.entity.Roles;
import com.ra.inventory_management.model.entity.UserGoogle;
import com.ra.inventory_management.model.entity.Users;
import com.ra.inventory_management.reponsitory.UserGoogleRepository;
import com.ra.inventory_management.reponsitory.UserRepository;

import com.ra.inventory_management.service.RoleService;

import com.ra.inventory_management.service.impl.AuthServiceIMPL;
import com.ra.inventory_management.service.impl.UserServiceIMPL;
import com.ra.inventory_management.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/app/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserServiceIMPL userServiceIMPL;

    @Autowired
    private AuthServiceIMPL authService;



    @Autowired
    private UserRepository userRepository;


    @Autowired
    private UserGoogleRepository userGoogleRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;



    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request.getUsername(), request.getPassword());

        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Mật khẩu không khớp!");
        }

        try {
            // Xử lý đăng ký người dùng
            Users user = userServiceIMPL.handleRegister(request);
            return ResponseEntity.ok("Đăng ký thành công! Email đã được gửi.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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

    @PostMapping("/oauth-login")
    public ResponseEntity<?> oauthLogin(Authentication authentication) {
        try {
            // Lấy thông tin từ token Google
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String email = jwt.getClaim("email");
            String name = jwt.getClaim("name");
            String avatar = jwt.getClaim("picture");

            if (email == null) {
                throw new IllegalArgumentException("Email not found in Google token");
            }

            // Kiểm tra xem đã tồn tại trong bảng UserGoogle chưa
            UserGoogle userGoogle = userGoogleRepository.findByEmail(email)
                    .map(existing -> {
                        existing.setUpdatedAt(LocalDateTime.now());
                        return existing;
                    })
                    .orElseGet(() -> {
                        UserGoogle newUser = UserGoogle.builder()
                                .code(Users.generateUserCode())
                                .username(email)
                                .email(email)
                                .avatar(avatar)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .deleteFg(false)
                                .build();
                        return userGoogleRepository.save(newUser);
                    });

            // Kiểm tra và tạo Users tương ứng nếu chưa có
            Users user = userRepository.findByEmail(email).orElseGet(() -> {
                Roles defaultRole = Roles.builder().roleName(ERoles.ROLE_SHIPPER).build();
                Users newUser = Users.builder()
                        .userCode(Users.generateUserCode())
                        .username(email)
                        .password("") // Không dùng password với OAuth
                        .email(email)
                        .fullname(name)
                        .phone("")
                        .address("")
                        .activeFlag(1)
                        .roles(new ArrayList<>(List.of(defaultRole)))
                        .build();
                return userRepository.save(newUser);
            });

            // Lấy danh sách roles
            List<String> roles = user.getRoles().stream()
                    .map(role -> "ROLE_" + role.getRoleName().name())
                    .collect(Collectors.toList());

            // Tạo JWT có chứa claim roles
            String newToken = jwtTokenUtil.generateToken(user);
            Map<String, Object> result = new HashMap<>();
            result.put("accessToken", newToken);
            result.put("code", user.getUserCode()); // Đây là mã shipper

            return ResponseEntity.ok(new BaseResponse<>(result));


        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new BaseResponse<String>(null, "Failed to process OAuth login: " + e.getMessage()));

        }
    }


    @PostMapping(value = "oauth-register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> oauthRegister(JwtAuthenticationToken token) {
        Map<String, Object> claims = token.getTokenAttributes();
        UserGoogle response = authService.registerOAuth(claims);
        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }



}