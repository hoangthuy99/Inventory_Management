package com.ra.inventory_management.reponsitory;

import com.ra.inventory_management.model.entity.UserGoogle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGoogleRepository extends JpaRepository<UserGoogle, Integer> {
    Optional<UserGoogle> findByEmail(String email);
}
