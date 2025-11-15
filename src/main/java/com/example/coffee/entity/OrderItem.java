package com.example.coffee.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "order_item")
@Data
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long orderId;  // 订单ID
    
    @Column(nullable = false)
    private String productName;  // 商品名称
    
    @Column(nullable = false)
    private Double productPrice;  // 商品价格
    
    @Column(nullable = false)
    private Integer quantity;  // 购买数量
    
    @Column(nullable = false)
    private Double totalPrice;  // 总价（价格×数量）
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", insertable = false, updatable = false)
    @JsonIgnore
    private Order order;
}
