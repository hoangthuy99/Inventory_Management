package com.ra.inventory_management.reponsitory;


import com.ra.inventory_management.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    @Query("SELECT u from Users u WHERE u.username like ?1% ")
    List<Users> searchAllByUsername(String keyword);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Users> findByUserCode(String userCode);

}
