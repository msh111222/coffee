package com.example.coffee.controller;

import com.example.coffee.common.Result;
import com.example.coffee.dto.OneKeyLoginRequest;
import com.example.coffee.entity.User;
import com.example.coffee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 一键登录
     */
    @PostMapping("/oneKeyLogin")
    public Result oneKeyLogin(@RequestBody OneKeyLoginRequest request) {
        try {
            User user = userService.oneKeyLogin(
                    request.getCode(),
                    request.getNickName(),
                    request.getAvatarUrl()
            );
            return Result.success("登录成功", user);
        } catch(Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info/{id}")
    public Result getUserInfo(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return Result.success(user);
        }
        return Result.error("用户不存在");
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result updateUserInfo(@RequestBody User user) {
        try {
            if (user.getId() == null) {
                return Result.error("用户ID不能为空");
            }

            User updatedUser = userService.updateUserInfo(user);
            return Result.success("更新成功", updatedUser);
        } catch(Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 充值接口
     */
    @PostMapping("/recharge")
    public Result recharge(@RequestBody RechargeRequest request) {
        try {
            if (request.getId() == null) {
                return Result.error("用户ID不能为空");
            }
            if (request.getAmount() == null || request.getAmount() <= 0) {
                return Result.error("充值金额必须大于0");
            }

            User updatedUser = userService.rechargePoints(request.getId(), request.getAmount());
            return Result.success("充值成功", updatedUser);
        } catch(Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取充值历史
     */
    @GetMapping("/rechargeHistory/{userId}")
    public Result getRechargeHistory(@PathVariable Long userId) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }

            Object rechargeList = userService.getRechargeHistory(userId);
            return Result.success("获取成功", rechargeList);
        } catch(Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 上传头像
     */
    @PostMapping("/uploadAvatar")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.error("文件为空");
            }

            // 检查文件类型
            String fileName = file.getOriginalFilename();
            if (!fileName.matches(".*\\.(jpg|jpeg|png|gif)$")) {
                return Result.error("只支持jpg、png、gif格式的图片");
            }

            // 生成唯一文件名
            String newFileName = System.currentTimeMillis() + "_" + fileName;

            // 上传到远程服务器路径
            String staticPath = "/www/coffee/coffee/static";
            File staticDir = new File(staticPath);
            if (!staticDir.exists()) {
                staticDir.mkdirs();
            }

            // 保存文件到服务器
            String filePath = staticPath + File.separator + newFileName;
            file.transferTo(new File(filePath));

            // 返回服务器的访问路径
            String imageUrl = "http://8.148.153.92:8081/static/" + newFileName;
            return Result.success("上传成功", imageUrl);
        } catch(Exception e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }

    /**
     * 创建充值订单
     */
    @PostMapping("/createRecharge")
    public Result createRecharge(@RequestBody CreateRechargeRequest request) {
        try {
            if (request.getUserId() == null) {
                return Result.error("用户ID不能为空");
            }
            if (request.getAmount() == null || request.getAmount() <= 0) {
                return Result.error("充值金额必须大于0");
            }

            String outTradeNo = userService.createRecharge(request.getUserId(), request.getAmount());
            return Result.success("创建订单成功", outTradeNo);
        } catch(Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 模拟支付成功
     */
    @PostMapping("/mockPaySuccess")
    public Result mockPaySuccess(@RequestBody MockPayRequest request) {
        try {
            if (request.getOutTradeNo() == null || request.getOutTradeNo().isEmpty()) {
                return Result.error("商户订单号不能为空");
            }
            if (request.getTransactionId() == null || request.getTransactionId().isEmpty()) {
                return Result.error("微信支付单号不能为空");
            }

            userService.mockPaySuccess(request.getOutTradeNo(), request.getTransactionId());
            return Result.success("支付成功");
        } catch(Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 充值请求 DTO
     */
    public static class RechargeRequest {
        private Long id;
        private Integer amount;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }
    }

    /**
     * 创建充值订单请求 DTO
     */
    public static class CreateRechargeRequest {
        private Long userId;
        private Integer amount;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }
    }

    /**
     * 模拟支付请求 DTO
     */
    public static class MockPayRequest {
        private String outTradeNo;
        private String transactionId;

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }
    }
}