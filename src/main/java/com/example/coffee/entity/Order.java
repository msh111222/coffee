package com.example.coffee.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;  // 用户ID
    
    @Column(unique = true, nullable = false)
    private String orderNo;  // 订单号
    
    @Column(nullable = false)
    private Double totalAmount;  // 订单总金额
    
    @Column(columnDefinition = "VARCHAR(20) DEFAULT '待支付'")
    private String status = "待支付";  // 支付状态：待支付、已支付
    
    @Column(columnDefinition = "VARCHAR(20) DEFAULT '待发货'")
    private String shipStatus = "待发货";  // 发货状态：待发货、已发货、待收货、已收货
    
    @Column(columnDefinition = "VARCHAR(20) DEFAULT '无'")
    private String refundStatus = "无";  // 退款状态：无、申请中、已退款
    
    @Column(nullable = false)
    private Long addressId;  // 收货地址ID
    
    @Column(columnDefinition = "TEXT")
    private String remark;  // 备注

    @Column
    private String trackingNumber;  // 物流单号

    @Column(columnDefinition = "VARCHAR(50)")
    private String expressCompany;  // 快递公司（如：顺丰、圆通、韵达等）

    @Temporal(TemporalType.TIMESTAMP)
    private Date shipTime;  // 发货时间

    @Temporal(TemporalType.TIMESTAMP)
    private Date refundTime;  // 退款时间

    @Temporal(TemporalType.TIMESTAMP)
    private Date receiveTime;  // 收货时间
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items;  // 订单项目列表
    
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
