package com.example.coffee.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "admin")
@Data
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String realName;

    /**
     * 管理员角色：
     * 1 - 系统管理员（全权管理）
     * 2 - 商品管理员（管理商品）
     * 3 - 订单管理员（管理订单）
     */
    @Column(nullable = false)
    private Integer role;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

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
