package com.example.coffee.repository;

import com.example.coffee.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
   List<Order> findByUserId(Long userId);

   Order findByOrderNo(String orderNo);

   List<Order> findByUserIdAndStatus(Long userId, String status);

   List<Order> findByUserIdAndShipStatus(Long userId, String shipStatus);

   List<Order> findByUserIdAndRefundStatus(Long userId, String refundStatus);
}
