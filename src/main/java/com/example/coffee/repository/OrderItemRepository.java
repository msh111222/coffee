package com.example.coffee.repository;

import com.example.coffee.entity.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
   List<OrderItem> findByOrderId(Long orderId);

   void deleteByOrderId(Long orderId);
}
