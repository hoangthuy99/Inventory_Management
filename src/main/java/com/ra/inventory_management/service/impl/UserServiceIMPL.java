package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.model.dto.request.RegisterRequest;
import com.ra.inventory_management.common.ERoles;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.entity.Roles;
import com.ra.inventory_management.model.entity.Users;


import com.ra.inventory_management.reponsitory.RoleRepository;
import com.ra.inventory_management.reponsitory.UserRepository;
import com.ra.inventory_management.sercurity.UserDetail.UserDetailService;
import com.ra.inventory_management.sercurity.UserDetail.UserPrincipal;
import com.ra.inventory_management.service.EmailService;
import com.ra.inventory_management.service.RoleService;
import com.ra.inventory_management.service.UserService;
import com.ra.inventory_management.util.PageableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceIMPL implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        user.setFullname(registerRequest.getFullname());
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
    public Page<Users> search(SearchRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users userPrincipal = (Users) authentication.getPrincipal();

        Pageable pageable = PageableUtil.create(request.getPageNum(), request.getPageSize(), request.getSortBy(), request.getSortType());

        List<Users> filteredUsers = userRepository.searchUsers(request.getSearchKey(), request.getStatus(), pageable)
                .stream()
                .filter(u -> !u.getId().equals(userPrincipal.getId())) // Loại bỏ chính user hiện tại
                .collect(Collectors.toList()); // Thu thập vào List

        return new PageImpl<>(filteredUsers, pageable, filteredUsers.size());
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
    public Users update(RegisterRequest request, Long id) {
        Users userOld = findById(id);
        if (!request.getUsername().equals(userOld.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new RuntimeException("Username is exists");
            }
        }

        Roles roles = roleRepository.findById(request.getRoleId()).
                orElseThrow(() -> new IllegalArgumentException("Vai trò không tồn tại vai trò với id: " + request.getRoleId()));

        Set<Roles> rolesExits = userOld.getRoles();
        rolesExits.removeAll(userOld.getRoles());
        rolesExits.add(roles);


        userOld.setFullname(request.getFullname());
        userOld.setAddress(request.getAddress());
        userOld.setPhone(request.getPhone());
        userOld.setEmail(request.getEmail());
        userOld.setActiveFlag(request.getActiveFlag());
        userOld.setRoles(rolesExits);

        return userRepository.save(userOld);
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
