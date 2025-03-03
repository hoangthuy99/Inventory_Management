package com.ra.inventory_management.service;


import com.ra.inventory_management.model.dto.request.RegisterRequest;
import com.ra.inventory_management.model.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface UserService {


    Users handleRegister(RegisterRequest registerRequest);

    Page<Users> getAll(Pageable pageable);
    Users findById(Long id);
    void delete(Long id);
    Users save(Users users);
    Users updateAcc(Users user, Long id);
    Optional<Users> findByUsername(String username);
    List<Users> searchByName(String keyword);
}
