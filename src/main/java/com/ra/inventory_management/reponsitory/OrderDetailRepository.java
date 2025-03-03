package com.ra.inventory_management.reponsitory;

import com.ra.inventory_management.model.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetails, Long> {
    // Lấy danh sách chi tiết đơn hàng theo order_id
    List<OrderDetails> findByOrderId(Long orderId);

    // Lấy danh sách chi tiết đơn hàng theo product_id
    List<OrderDetails> findByProductInfoId(Long productId);

    // Lấy danh sách chi tiết đơn hàng theo order_id và product_id
    OrderDetails findByOrderIdAndProductInfoId(Long orderId, Long productId);
    @Query("SELECT o FROM OrderDetails o WHERE o.productInfo.name LIKE CONCAT(:keyword, '%')")
    List<OrderDetails> searchByProductName(@Param("keyword") String keyword);

}
