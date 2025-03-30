package com.ra.inventory_management.reponsitory;

import com.ra.inventory_management.model.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Long> {
}
