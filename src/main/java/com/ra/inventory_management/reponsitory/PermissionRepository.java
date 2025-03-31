package com.ra.inventory_management.reponsitory;

import com.ra.inventory_management.model.entity.Permission;
import com.ra.inventory_management.model.entity.Roles;
import com.ra.inventory_management.model.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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
    @EntityGraph(attributePaths = {"permissionDetails"}) // Tự động load danh sách PermissionDetail
    @Query("""
        SELECT p FROM Permission p 
        WHERE (:searchKey IS NULL OR :searchKey = '' 
               OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchKey, '%')) 
               OR LOWER(p.menu.name) LIKE LOWER(CONCAT('%', :searchKey, '%')))
        AND (:status IS NULL OR :status = -1 OR p.activeFlag = :status)
    """)
    Page<Permission> searchPermission(@Param("searchKey") String searchKey,
                                      @Param("status") Integer status,
                                      Pageable pageable);

}
