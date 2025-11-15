package com.example.coffee.service;

import com.example.coffee.entity.Admin;
import com.example.coffee.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    /**
     * 管理员登录
     */
    public Admin login(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        
        if (admin == null) {
            throw new RuntimeException("管理员不存在");
        }
        
        if (!admin.getIsActive()) {
            throw new RuntimeException("该管理员账号已禁用");
        }
        
        if (!admin.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }
        
        // 更新最后登录时间
        admin.setLastLoginTime(new Date());
        adminRepository.save(admin);
        
        return admin;
    }

    /**
     * 根据 ID 获取管理员信息
     */
    public Admin getAdminById(Long adminId) {
        return adminRepository.findById(adminId).orElse(null);
    }

    /**
     * 创建管理员（初始化时使用）
     */
    public Admin createAdmin(String username, String password, String realName, Integer role) {
        Admin existingAdmin = adminRepository.findByUsername(username);
        if (existingAdmin != null) {
            throw new RuntimeException("用户名已存在");
        }

        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setRealName(realName);
        admin.setRole(role);
        admin.setIsActive(true);
        
        return adminRepository.save(admin);
    }
}
