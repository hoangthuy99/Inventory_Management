package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.common.ERoles;
import com.ra.inventory_management.model.dto.response.JwtResponse;
import com.ra.inventory_management.model.entity.*;
import com.ra.inventory_management.reponsitory.*;
import com.ra.inventory_management.service.AuthService;
import com.ra.inventory_management.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceIMPL implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserGoogleRepository userGoogleRepository;
    private final RoleRepository roleRepository;



    @Override
    public JwtResponse login(String username, String password) {
        Optional<Users> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new BadCredentialsException("Invalid username or password");
        }

        Users existingUser = userOptional.get();

        // check password
        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new BadCredentialsException("Wrong username or password");
        }

        // authenticate with spring security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username,
                password,
                existingUser.getAuthorities()
        );
        authenticationManager.authenticate(authenticationToken);

        // generate token
        String token = jwtTokenUtil.generateToken(userOptional.get());

        return JwtResponse.builder()
                .accessToken(token)
                .email(existingUser.getEmail())
                .fullName(existingUser.getFullname())
                .username(existingUser.getUsername())
                .roles(existingUser.getRoles().stream().map(r -> String.valueOf(r.getRoleName())).toList())
                .build();
    }

    @Override
    public UserGoogle registerOAuth(Map<String, Object> claims) {
        UserGoogle userGoogleExisted = userGoogleRepository.findByEmail(claims.get("email").toString()).orElse(null);

        if (userGoogleExisted != null) {
            throw new DuplicateKeyException("User already exists");
        }

        UserGoogle userGoogle = UserGoogle.builder()
                .username(claims.get("name").toString())
                .email(claims.get("email").toString())
                .avatar(claims.get("picture").toString())
                .createdAt(LocalDateTime.now())
                .deleteFg(false)
                .build();

        userGoogleRepository.save(userGoogle);

        return userGoogle;
    }

    @Override
    public JwtResponse oauthLogin(Map<String, Object> claims) {
        UserGoogle userGoogleExisted = userGoogleRepository.findByEmail(claims.get("email").toString()).orElse(null);

        // if not exist save to database
        StringBuilder prefix = new StringBuilder();
        Arrays.stream(claims.get("name").toString().split("\\s+")) // Tách theo khoảng trắng
                .forEach(word -> prefix.append(word.charAt(0)));
        String code = prefix + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));

        if (userGoogleExisted == null) {
            UserGoogle userGoogle = UserGoogle.builder()
                    .code(code)
                    .username(claims.get("name").toString())
                    .email(claims.get("email").toString())
                    .avatar(claims.get("picture").toString())
                    .createdAt(LocalDateTime.now())
                    .deleteFg(false)
                    .build();
            userGoogleExisted = userGoogleRepository.save(userGoogle);
        }

        return JwtResponse.builder()
                .email(userGoogleExisted.getEmail())
                .username(userGoogleExisted.getUsername())
                .fullName(userGoogleExisted.getUsername())
                .roles(List.of("ROLE_STAFF"))
                .build();
    }


    }


