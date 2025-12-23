package com.example.coffee.repository;

import com.example.coffee.entity.Recharge;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Long> {
   List<Recharge> findByUserIdOrderByCreateTimeDesc(Long userId);

   Recharge findByOutTradeNo(String outTradeNo);
}
