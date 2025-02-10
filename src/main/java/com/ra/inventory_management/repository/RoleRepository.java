package com.ra.inventory_management.repository;


import com.ra.inventory_management.model.entity.ERoles;
import com.ra.inventory_management.model.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Roles findByRoleName(ERoles name);
}
