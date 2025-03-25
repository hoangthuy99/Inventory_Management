package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.model.dto.request.RegisterRequest;
import com.ra.inventory_management.common.ERoles;
import com.ra.inventory_management.model.entity.Roles;
import com.ra.inventory_management.model.entity.Users;


import com.ra.inventory_management.reponsitory.RoleRepository;
import com.ra.inventory_management.reponsitory.UserRepository;
import com.ra.inventory_management.service.EmailService;
import com.ra.inventory_management.service.RoleService;
import com.ra.inventory_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


@Service
public class UserServiceIMPL implements UserService {
    @Autowired
    private UserRepository userRepository;

    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private EmailService emailService;


    @Override
    public Users handleRegister(RegisterRequest registerRequest) {
        Logger logger = LoggerFactory.getLogger(getClass());

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại!");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại!");
        }

        Users user = new Users();
        user.setFullName(registerRequest.getFullName());
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());

        //  Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        //  Gán quyền mặc định
        Set<Roles> defaultRoles = new HashSet<>();
        defaultRoles.add(roleRepository.findByRoleName(ERoles.ROLE_STAFF));
        user.setRoles(defaultRoles);

        //  Tạo mã xác nhận
        String verificationCode = UUID.randomUUID().toString();
        user.setUserCode(verificationCode); // Tạm dùng field avatar để lưu mã xác nhận
        user.setActiveFlag(0); // 0: chưa xác nhận

        try {
            userRepository.save(user);

            //  Gửi email xác nhận
            emailService.sendVerificationEmail(user.getEmail(), verificationCode);

            return user;
        } catch (DataIntegrityViolationException e) {
            logger.error("Lỗi khi lưu user: " + e.getRootCause().getMessage());
            throw new IllegalArgumentException("Lỗi khi lưu user: " + e.getRootCause().getMessage());
        }
    }

    @Override
    public Page<Users> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Users findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Users save(Users users) {
        return userRepository.save(users);
    }

    @Override
    public Users updateAcc(Users user, Long id) {
        Users userOld = findById(id);
        if(!user.getUsername().equals(userOld.getUsername())) {
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new RuntimeException("Username is exists");
            }
        }

        Set<Roles> roles = userOld.getRoles();

        Users users = Users.builder()
                .id(user.getId())
                .userCode(user.getUserCode())
                .username(user.getUsername())
                .fullName(userOld.getFullName())
                .email(user.getEmail())
                .address(user.getAddress())
                .phone(user.getPhone())
                .activeFlag(user.getActiveFlag())
                .createdDate(user.getCreatedDate())
                .updateDate(user.getUpdateDate())
                .roles(roles)
                .build();
        return userRepository.save(users);
    }

    @Override
    public Optional<Users> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public List<Users> searchByName(String keyword) {
        return null;
    }


}
