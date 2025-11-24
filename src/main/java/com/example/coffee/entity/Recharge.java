package com.example.coffee.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "recharge")
@Data
@NoArgsConstructor
public class Recharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Integer amount; // 充值金额

    @Column(nullable = false)
    private String status = "success"; // 充值状态：success 成功，failed 失败，pending 待处理

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

        // 微信支付相关字段
    @Column(columnDefinition = "VARCHAR(64)")
    private String transactionId;  // 微信支付单号
    
    @Column(columnDefinition = "VARCHAR(64)")
    private String outTradeNo;  // 商户单号（商户系统内部的订单号）
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date payTime;  // 支付时间

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = new Date();
        updateTime = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = new Date();
    }
}

