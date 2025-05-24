package com.ra.inventory_management.reponsitory;

import com.ra.inventory_management.model.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c from Customer c WHERE c.name like ?1% ")
    List<Customer> searchCustomerByName(String keyword);

    boolean existsByCusCode(String cusCode);

    List<Customer> findByActiveFlag(int i);

    @Query("""
                SELECT c FROM Customer c
                    WHERE (:status IS NULL OR :status = -1 OR c.activeFlag = :status)
                    AND
                    (
                        :searchKey IS NULL OR :searchKey = ''
                        OR c.cusCode LIKE %:searchKey%
                        OR c.name LIKE %:searchKey%
                    )
            """)
    Page<Customer> searchCustomer(
            @Param("searchKey") String searchKey,
            @Param("status") Integer status,
            Pageable pageable
    );
}
