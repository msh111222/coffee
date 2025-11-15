package com.example.coffee.repository;

import com.example.coffee.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    /**
     * 根据用户名查询管理员
     */
    Admin findByUsername(String username);
}
