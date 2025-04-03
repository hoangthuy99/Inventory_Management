package com.ra.inventory_management.reponsitory;

import com.ra.inventory_management.model.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Long> {

    boolean existsByName (String name);
    @Query("""
                SELECT m FROM Menu m
                    where (
                        :searchKey IS NULL OR :searchKey = ''
                        OR m.code LIKE %:searchKey%
                        OR m.name LIKE %:searchKey%
                    )
                    AND (:status IS NULL OR :status = -1 OR m.activeFlag = :status)
            """)
    Page<Menu> searchMenu(
            @Param("searchKey") String searchKey,
            @Param("status") Integer status,
            Pageable pageable
    );
    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.roles WHERE m.id = :id")
    Optional<Menu> findByIdWithRoles(@Param("id") Long id);

}
