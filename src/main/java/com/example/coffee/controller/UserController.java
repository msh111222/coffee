package com.example.coffee.controller;

import com.example.coffee.common.Result;
import com.example.coffee.dto.OneKeyLoginRequest;
import com.example.coffee.entity.User;
import com.example.coffee.service.UserService;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping({"/api/user"})
@CrossOrigin
public class UserController {
   @Autowired
   private UserService userService;

   @PostMapping({"/oneKeyLogin"})
   public Result oneKeyLogin(@RequestBody OneKeyLoginRequest request) {
      try {
         User user = this.userService.oneKeyLogin(request.getCode(), request.getNickName(), request.getAvatarUrl());
         return Result.success("登录成功", user);
      } catch (Exception var3) {
         return Result.error(var3.getMessage());
      }
   }

   @GetMapping({"/info/{id}"})
   public Result getUserInfo(@PathVariable Long id) {
      User user = this.userService.getUserById(id);
      return user != null ? Result.success(user) : Result.error("用户不存在");
   }

   @PutMapping({"/update"})
   public Result updateUserInfo(@RequestBody User user) {
      try {
         if (user.getId() == null) {
            return Result.error("用户ID不能为空");
         } else {
            User updatedUser = this.userService.updateUserInfo(user);
            return Result.success("更新成功", updatedUser);
         }
      } catch (Exception var3) {
         return Result.error(var3.getMessage());
      }
   }

   @PostMapping({"/recharge"})
   public Result recharge(@RequestBody UserController.RechargeRequest request) {
      try {
         if (request.getId() == null) {
            return Result.error("用户ID不能为空");
         } else if (request.getAmount() != null && request.getAmount() > 0) {
            User updatedUser = this.userService.rechargePoints(request.getId(), request.getAmount());
            return Result.success("充值成功", updatedUser);
         } else {
            return Result.error("充值金额必须大于0");
         }
      } catch (Exception var3) {
         return Result.error(var3.getMessage());
      }
   }

   @GetMapping({"/rechargeHistory/{userId}"})
   public Result getRechargeHistory(@PathVariable Long userId) {
      try {
         if (userId == null) {
            return Result.error("用户ID不能为空");
         } else {
            Object rechargeList = this.userService.getRechargeHistory(userId);
            return Result.success("获取成功", rechargeList);
         }
      } catch (Exception var3) {
         return Result.error(var3.getMessage());
      }
   }

   @PostMapping({"/uploadAvatar"})
   public Result uploadAvatar(@RequestParam("file") MultipartFile file) {
      try {
         if (file.isEmpty()) {
            return Result.error("文件为空");
         } else {
            String fileName = file.getOriginalFilename();
            if (!fileName.matches(".*\\.(jpg|jpeg|png|gif)$")) {
               return Result.error("只支持jpg、png、gif格式的图片");
            } else {
               long var10000 = System.currentTimeMillis();
               String newFileName = var10000 + "_" + fileName;
               String staticPath = "/www/coffee/coffee/static";
               File staticDir = new File(staticPath);
               if (!staticDir.exists()) {
                  staticDir.mkdirs();
               }

               String filePath = staticPath + File.separator + newFileName;
               file.transferTo(new File(filePath));
               String imageUrl = "http://8.148.153.92:8081/static/" + newFileName;
               return Result.success("上传成功", imageUrl);
            }
         }
      } catch (Exception var8) {
         return Result.error("上传失败: " + var8.getMessage());
      }
   }

   @PostMapping({"/createRecharge"})
   public Result createRecharge(@RequestBody UserController.CreateRechargeRequest request) {
      try {
         if (request.getUserId() == null) {
            return Result.error("用户ID不能为空");
         } else if (request.getAmount() != null && request.getAmount() > 0) {
            String outTradeNo = this.userService.createRecharge(request.getUserId(), request.getAmount());
            return Result.success("创建订单成功", outTradeNo);
         } else {
            return Result.error("充值金额必须大于0");
         }
      } catch (Exception var3) {
         return Result.error(var3.getMessage());
      }
   }

   @PostMapping({"/mockPaySuccess"})
   public Result mockPaySuccess(@RequestBody UserController.MockPayRequest request) {
      try {
         if (request.getOutTradeNo() != null && !request.getOutTradeNo().isEmpty()) {
            if (request.getTransactionId() != null && !request.getTransactionId().isEmpty()) {
               this.userService.mockPaySuccess(request.getOutTradeNo(), request.getTransactionId());
               return Result.success("支付成功");
            } else {
               return Result.error("微信支付单号不能为空");
            }
         } else {
            return Result.error("商户订单号不能为空");
         }
      } catch (Exception var3) {
         return Result.error(var3.getMessage());
      }
   }

   public static class RechargeRequest {
      private Long id;
      private Integer amount;

      public Long getId() {
         return this.id;
      }

      public void setId(Long id) {
         this.id = id;
      }

      public Integer getAmount() {
         return this.amount;
      }

      public void setAmount(Integer amount) {
         this.amount = amount;
      }
   }

   public static class CreateRechargeRequest {
      private Long userId;
      private Integer amount;

      public Long getUserId() {
         return this.userId;
      }

      public void setUserId(Long userId) {
         this.userId = userId;
      }

      public Integer getAmount() {
         return this.amount;
      }

      public void setAmount(Integer amount) {
         this.amount = amount;
      }
   }

   public static class MockPayRequest {
      private String outTradeNo;
      private String transactionId;

      public String getOutTradeNo() {
         return this.outTradeNo;
      }

      public void setOutTradeNo(String outTradeNo) {
         this.outTradeNo = outTradeNo;
      }

      public String getTransactionId() {
         return this.transactionId;
      }

      public void setTransactionId(String transactionId) {
         this.transactionId = transactionId;
      }
   }
}
