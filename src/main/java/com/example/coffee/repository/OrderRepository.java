package com.example.coffee.repository;

import com.example.coffee.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    Order findByOrderNo(String orderNo);
    
    // 按支付状态查询
    List<Order> findByUserIdAndStatus(Long userId, String status);
    
    // 按发货状态查询
    List<Order> findByUserIdAndShipStatus(Long userId, String shipStatus);
    
    // 按退款状态查询
    List<Order> findByUserIdAndRefundStatus(Long userId, String refundStatus);
}
