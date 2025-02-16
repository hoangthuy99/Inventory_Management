package com.ra.inventory_management.reponsitory;


import com.ra.inventory_management.model.entity.product.ERoles;
import com.ra.inventory_management.model.entity.product.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Roles findByRoleName(ERoles name);
}
