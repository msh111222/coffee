package com.example.coffee.repository;

import com.example.coffee.entity.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Long> {
    // 根据 userId 获取充值记录，按时间降序
    List<Recharge> findByUserIdOrderByCreateTimeDesc(Long userId);
    
    // 根据商户订单号查找充值记录
    Recharge findByOutTradeNo(String outTradeNo);
}

