package com.ra.inventory_management.controller;


import com.ra.inventory_management.model.dto.request.LoginRequest;
import com.ra.inventory_management.model.dto.request.RegisterRequest;
import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.dto.response.JwtResponse;
import com.ra.inventory_management.model.dto.response.RegisterResponse;
import com.ra.inventory_management.model.entity.UserGoogle;
import com.ra.inventory_management.model.entity.Users;
import com.ra.inventory_management.service.AuthService;
import com.ra.inventory_management.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/app/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


//    @PostMapping("register")
//    public String save(@Valid @ModelAttribute("user") RegisterRequest registerRequest, BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            return "auth/register";
//        }
//
//        try {
//            userService.handleRegister(registerRequest);
//        } catch (IllegalArgumentException e) {
//            model.addAttribute("err", e.getMessage());
//            return "auth/register";
//        }
//
//        return "redirect:/login";
//    }
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request.getUsername(), request.getPassword());
        return  ResponseEntity.ok(new BaseResponse<>(response));
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

}