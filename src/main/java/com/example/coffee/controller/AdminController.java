package com.example.coffee.controller;

import com.example.coffee.entity.Admin;
import com.example.coffee.service.AdminService;
import com.example.coffee.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {
    @Autowired
    private AdminService adminService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");

            if (username == null || username.isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (password == null || password.isEmpty()) {
                return Result.error("密码不能为空");
            }

            Admin admin = adminService.login(username, password);

            // 返回管理员信息（不返回密码）
            Map<String, Object> data = new HashMap<>();
            data.put("id", admin.getId());
            data.put("username", admin.getUsername());
            data.put("realName", admin.getRealName());
            data.put("role", admin.getRole());

            return Result.success("登录成功", data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取管理员信息
     */
    @GetMapping("/{adminId}")
    public Result getAdmin(@PathVariable Long adminId) {
        try {
            Admin admin = adminService.getAdminById(adminId);
            if (admin == null) {
                return Result.error("管理员不存在");
            }

            Map<String, Object> data = new HashMap<>();
            data.put("id", admin.getId());
            data.put("username", admin.getUsername());
            data.put("realName", admin.getRealName());
            data.put("role", admin.getRole());

            return Result.success("获取成功", data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
