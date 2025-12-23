package com.example.coffee.controller;

import com.example.coffee.common.Result;
import com.example.coffee.entity.Admin;
import com.example.coffee.service.AdminService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/admin"})
@CrossOrigin
public class AdminController {
   @Autowired
   private AdminService adminService;

   @PostMapping({"/login"})
   public Result login(@RequestBody Map<String, String> request) {
      try {
         String username = (String)request.get("username");
         String password = (String)request.get("password");
         if (username != null && !username.isEmpty()) {
            if (password != null && !password.isEmpty()) {
               Admin admin = this.adminService.login(username, password);
               Map<String, Object> data = new HashMap();
               data.put("id", admin.getId());
               data.put("username", admin.getUsername());
               data.put("realName", admin.getRealName());
               data.put("role", admin.getRole());
               return Result.success("登录成功", data);
            } else {
               return Result.error("密码不能为空");
            }
         } else {
            return Result.error("用户名不能为空");
         }
      } catch (Exception var6) {
         return Result.error(var6.getMessage());
      }
   }

   @GetMapping({"/{adminId}"})
   public Result getAdmin(@PathVariable Long adminId) {
      try {
         Admin admin = this.adminService.getAdminById(adminId);
         if (admin == null) {
            return Result.error("管理员不存在");
         } else {
            Map<String, Object> data = new HashMap();
            data.put("id", admin.getId());
            data.put("username", admin.getUsername());
            data.put("realName", admin.getRealName());
            data.put("role", admin.getRole());
            return Result.success("获取成功", data);
         }
      } catch (Exception var4) {
         return Result.error(var4.getMessage());
      }
   }
}
