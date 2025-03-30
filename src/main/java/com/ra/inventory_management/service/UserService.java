package com.ra.inventory_management.service;


import com.ra.inventory_management.model.dto.request.RegisterRequest;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface UserService {


    Users handleRegister(RegisterRequest registerRequest);

    Page<Users> search(SearchRequest request);

    Users findById(Long id);

    void delete(Long id);

    Users save(Users users);

    Users update(RegisterRequest request, Long id);

    Optional<Users> findByUsername(String username);

    List<Users> searchByName(String keyword);
}
