package com.ra.inventory_management.reponsitory;

import com.ra.inventory_management.model.entity.Permission;
import com.ra.inventory_management.model.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {
    Optional<Permission> findByName(String name);
    @Query("SELECT p FROM Permission p JOIN Auth a ON p.id = a.permission.id WHERE a.roles = :role")
    List<Permission> findPermissionsByRole(@Param("role") Roles role);


}
