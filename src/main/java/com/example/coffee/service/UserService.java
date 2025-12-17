package com.example.coffee.service;

import com.example.coffee.entity.User;
import com.example.coffee.entity.Recharge;
import com.example.coffee.repository.UserRepository;
import com.example.coffee.repository.RechargeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RechargeRepository rechargeRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.appSecret}")
    private String appSecret;

    /**
     * 一键登录
     */
    public User oneKeyLogin(String code, String nickName, String avatarUrl) {
        try {
            // 1. 用 code 从微信获取 openId
            String openId = getOpenIdFromWeChat(code);

            System.out.println("========== 获取到 openId ==========");
            System.out.println("openId: " + openId);
            System.out.println("nickName: " + nickName);
            System.out.println("avatarUrl: " + avatarUrl);

            // 2. 查找用户
            User user = userRepository.findByOpenId(openId);

            if (user == null) {
                // 新用户 → 创建
                System.out.println("========== 创建新用户 ==========");
                user = new User();
                user.setOpenId(openId);
                user.setNickName(nickName);
                user.setAvatarUrl(avatarUrl);
                user.setPoints(0);
                user.setCreateTime(new Date());

                System.out.println("准备插入的用户信息:");
                System.out.println("  OpenId: " + user.getOpenId());
                System.out.println("  NickName: " + user.getNickName());
                System.out.println("  AvatarUrl: " + user.getAvatarUrl());
                System.out.println("  Points: " + user.getPoints());
                System.out.println("  CreateTime: " + user.getCreateTime());
            } else {
                // 老用户 → 更新信息
                System.out.println("========== 更新老用户 ==========");
                System.out.println("用户ID: " + user.getId());
                user.setUpdateTime(new Date());
            }

            // 保存用户
            System.out.println("========== 开始保存用户 ==========");
            User savedUser = userRepository.save(user);
            System.out.println("========== 用户保存成功 ==========");
            System.out.println("保存后的用户ID: " + savedUser.getId());
            System.out.println("保存后的openId: " + savedUser.getOpenId());

            return savedUser;

        } catch(Exception e) {
            System.out.println("========== 登录错误 ==========");
            System.out.println("错误信息: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("登录失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户信息
     */
    public User updateUserInfo(User user) {
        try {
            System.out.println("========== 更新用户信息 ==========");
            System.out.println("用户ID: " + user.getId());

            User existingUser = userRepository.findById(user.getId()).orElse(null);
            if (existingUser == null) {
                throw new Exception("用户不存在");
            }

            // 更新允许修改的字段
            if (user.getNickName() != null && !user.getNickName().isEmpty()) {
                System.out.println("更新昵称: " + user.getNickName());
                existingUser.setNickName(user.getNickName());
            }
            if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                System.out.println("更新头像: " + user.getAvatarUrl());
                existingUser.setAvatarUrl(user.getAvatarUrl());
            }

            existingUser.setUpdateTime(new Date());

            User updatedUser = userRepository.save(existingUser);
            System.out.println("========== 用户信息更新成功 ==========");

            return updatedUser;
        } catch(Exception e) {
            System.out.println("========== 更新错误 ==========");
            System.out.println("错误信息: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("更新失败: " + e.getMessage());
        }
    }

    /**
     * 用 code 从微信获取 openId
     */
    private String getOpenIdFromWeChat(String code) throws Exception {
        System.out.println("========== 微信验证开始 ==========");
        System.out.println("Code: " + code);
        System.out.println("AppId: " + appId);

        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId
                + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code";

        System.out.println("请求URL: " + url);

        try {
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("微信返回: " + response);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(response, Map.class);

            System.out.println("解析后: " + map);

            if (map.containsKey("errcode")) {
                String errMsg = "微信验证失败: " + map.get("errmsg");
                System.out.println("错误: " + errMsg);
                throw new Exception(errMsg);
            }

            String openId = (String) map.get("openid");
            System.out.println("获取的OpenId: " + openId);
            System.out.println("========== 微信验证完成 ==========");

            return openId;
        } catch(Exception e) {
            System.out.println("微信验证异常: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 根据 ID 获取用户信息
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    /**
     * 保存用户信息
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * 创建充值订单（只生成记录，不加积分）
     */
    @Transactional
    public String createRecharge(Long userId, Integer amount) {
        try {
            System.out.println("========== 创建充值订单开始 ==========");
            System.out.println("用户ID: " + userId);
            System.out.println("充值金额: " + amount);

            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                throw new Exception("用户不存在");
            }

            // 生成商户订单号
            String outTradeNo = "RCH" + System.currentTimeMillis();

            // 保存充值记录（状态为PENDING）
            Recharge recharge = new Recharge();
            recharge.setUserId(userId);
            recharge.setAmount(amount);
            recharge.setOutTradeNo(outTradeNo);
            recharge.setStatus("PENDING");
            rechargeRepository.save(recharge);
            
            System.out.println("========== 充值订单创建成功 ==========");
            System.out.println("商户订单号: " + outTradeNo);

            return outTradeNo;
        } catch(Exception e) {
            System.out.println("========== 创建充值订单失败 ==========");
            System.out.println("错误信息: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("创建充值订单失败: " + e.getMessage());
        }
    }

    /**
     * 处理充值支付成功的业务逻辑（加积分、更新状态）
     * 该方法可被 mockPaySuccess、微信支付回调等多个场景复用
     * 
     * @param recharge 充值记录（必须为 PENDING 状态）
     * @param transactionId 支付单号（微信支付单号或模拟单号）
     * @throws RuntimeException 如果用户不存在或其他异常
     */
    private void processRechargePaid(Recharge recharge, String transactionId) {
        // 查找用户
        User user = userRepository.findById(recharge.getUserId()).orElse(null);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 增加积分（1元 = 1积分）
        Integer currentPoints = user.getPoints() != null ? user.getPoints() : 0;
        Integer newPoints = currentPoints + recharge.getAmount();
        user.setPoints(newPoints);
        user.setUpdateTime(new Date());

        // 更新充值记录
        recharge.setStatus("SUCCESS");
        recharge.setTransactionId(transactionId);
        recharge.setPayTime(new Date());

        // 保存更新
        userRepository.save(user);
        rechargeRepository.save(recharge);

        System.out.println("原有积分: " + currentPoints);
        System.out.println("新增积分: " + recharge.getAmount());
        System.out.println("总积分: " + newPoints);
    }

    /**
     * 处理充值支付成功（对外公开方法，用于模拟支付、微信回调等场景）
     * 
     * @param outTradeNo 商户订单号
     * @param transactionId 支付单号（微信支付单号或模拟单号）
     * @throws RuntimeException 如果订单不存在或其他异常
     */
    @Transactional
    public void handleRechargePaid(String outTradeNo, String transactionId) {
        try {
            System.out.println("========== 处理充值支付 ==========");
            System.out.println("商户订单号: " + outTradeNo);
            System.out.println("微信支付单号: " + transactionId);

            // 查找充值记录
            Recharge recharge = rechargeRepository.findByOutTradeNo(outTradeNo);
            if (recharge == null) {
                throw new Exception("充值订单不存在");
            }

            // 如果状态不是待支付，直接返回
            if (!"PENDING".equals(recharge.getStatus())) {
                System.out.println("订单状态异常或已处理，当前状态: " + recharge.getStatus());
                return;
            }

            // 调用通用方法处理支付成功
            processRechargePaid(recharge, transactionId);

            System.out.println("========== 处理完成 ==========");
        } catch(Exception e) {
            System.out.println("========== 处理失败 ==========");
            System.out.println("错误信息: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("处理失败: " + e.getMessage());
        }
    }

    /**
     * 模拟支付成功（根据商户订单号加积分）
     */
    @Transactional
    public void mockPaySuccess(String outTradeNo, String transactionId) {
        try {
            System.out.println("========== 支付成功处理开始 ==========");
            System.out.println("商户订单号: " + outTradeNo);
            System.out.println("微信支付单号: " + transactionId);

            // 调用对外方法处理
            handleRechargePaid(outTradeNo, transactionId);

            System.out.println("========== 支付成功处理完成 ==========");
        } catch(Exception e) {
            System.out.println("========== 支付成功处理失败 ==========");
            System.out.println("错误信息: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("支付成功处理失败: " + e.getMessage());
        }
    }

    /**
     * 充值积分（保留原方法，标记为废弃）
     */
    @Deprecated
    public User rechargePoints(Long userId, Integer amount) {
       try {
           System.out.println("========== 充值开始 ==========");
           System.out.println("用户ID: " + userId);
           System.out.println("充值金额: " + amount);

           User user = userRepository.findById(userId).orElse(null);
           if (user == null) {
               throw new Exception("用户不存在");
           }

           // 增加积分（1元 = 1积分）
           Integer currentPoints = user.getPoints() != null ? user.getPoints() : 0;
           Integer newPoints = currentPoints + amount;
           user.setPoints(newPoints);
           user.setUpdateTime(new Date());

           System.out.println("原有积分: " + currentPoints);
           System.out.println("新增积分: " + amount);
           System.out.println("总积分: " + newPoints);

           User updatedUser = userRepository.save(user);
           
           // 保存充值记录
           Recharge recharge = new Recharge();
           recharge.setUserId(userId);
           recharge.setAmount(amount);
           recharge.setStatus("success");
           rechargeRepository.save(recharge);
           System.out.println("========== 充值记录已保存 ==========");

           System.out.println("========== 充值成功 ==========");

           return updatedUser;
       } catch(Exception e) {
           System.out.println("========== 充值失败 ==========");
           System.out.println("错误信息: " + e.getMessage());
           e.printStackTrace();
           throw new RuntimeException("充值失败: " + e.getMessage());
       }
    }

    /**
     * 获取充值历史
     */
    public List<Recharge> getRechargeHistory(Long userId) {
        try {
            System.out.println("========== 获取充值历史 ==========");
            System.out.println("用户ID: " + userId);

            List<Recharge> rechargeList = rechargeRepository.findByUserIdOrderByCreateTimeDesc(userId);
            System.out.println("充值记录数: " + rechargeList.size());

            return rechargeList;
        } catch(Exception e) {
            System.out.println("========== 获取充值历史失败 ==========");
            System.out.println("错误信息: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("获取充值历史失败: " + e.getMessage());
        }
    }
}
