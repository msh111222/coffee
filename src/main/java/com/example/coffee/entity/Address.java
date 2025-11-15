package com.example.coffee.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private String recipientName;
    private String phoneNumber;
    private String province;
    private String city;
    private String district;
    private String detail;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isDefault = false;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @PrePersist
    protected void onCreate() {
        if (isDefault == null) {
            isDefault = false;
        }
        createTime = new Date();
        updateTime = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = new Date();
    }
}