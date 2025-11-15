package com.example.coffee.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String openId;

    private String nickName;

    private String avatarUrl;

    private String phoneNumber;

    private String personalInfo;

    private Integer points = 0;

    private Long consumption = 0L;  // 消费金额（单位：元），用于计算星级

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