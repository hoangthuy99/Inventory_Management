package com.ra.inventory_management.reponsitory;


import com.ra.inventory_management.common.ERoles;
import com.ra.inventory_management.model.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Roles findByRoleName(ERoles name);
    Optional<Roles> findById(Long id);
}
