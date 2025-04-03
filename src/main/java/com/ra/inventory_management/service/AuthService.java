package com.ra.inventory_management.service;

import com.ra.inventory_management.common.ERoles;
import com.ra.inventory_management.model.dto.response.JwtResponse;
import com.ra.inventory_management.model.dto.response.RegisterResponse;
import com.ra.inventory_management.model.entity.UserGoogle;

import java.util.Map;

public interface AuthService {
    JwtResponse login(String username, String password);

    UserGoogle registerOAuth(Map<String, Object> claims);

    JwtResponse oauthLogin(Map<String, Object> claims);


}
