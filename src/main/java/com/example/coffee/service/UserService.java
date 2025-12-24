package com.example.coffee.service;

import com.example.coffee.entity.Recharge;
import com.example.coffee.entity.User;
import com.example.coffee.repository.RechargeRepository;
import com.example.coffee.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.example.coffee.dto.UpdateUserProfileRequest;
import com.example.coffee.dto.UserProfileResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
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
   private static final Pattern CN_MOBILE = Pattern.compile("^1[3-9]\\d{9}$");
   private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd");
   public User oneKeyLogin(String code, String nickName, String avatarUrl) {
      try {
         String openId = this.getOpenIdFromWeChat(code);
         System.out.println("========== 获取到 openId ==========");
         System.out.println("openId: " + openId);
         System.out.println("nickName: " + nickName);
         System.out.println("avatarUrl: " + avatarUrl);
         User user = this.userRepository.findByOpenId(openId);
         if (user == null) {
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
            System.out.println("========== 更新老用户 ==========");
            System.out.println("用户ID: " + user.getId());
            user.setUpdateTime(new Date());
         }

         System.out.println("========== 开始保存用户 ==========");
         User savedUser = (User)this.userRepository.save(user);
         System.out.println("========== 用户保存成功 ==========");
         System.out.println("保存后的用户ID: " + savedUser.getId());
         System.out.println("保存后的openId: " + savedUser.getOpenId());
         return savedUser;
      } catch (Exception var7) {
         System.out.println("========== 登录错误 ==========");
         System.out.println("错误信息: " + var7.getMessage());
         var7.printStackTrace();
         throw new RuntimeException("登录失败: " + var7.getMessage());
      }
   }

   public User updateUserInfo(User user) {
      try {
         System.out.println("========== 更新用户信息 ==========");
         System.out.println("用户ID: " + user.getId());
         User existingUser = (User)this.userRepository.findById(user.getId()).orElse(null);
         if (existingUser == null) {
            throw new Exception("用户不存在");
         } else {
            if (user.getNickName() != null && !user.getNickName().isEmpty()) {
               System.out.println("更新昵称: " + user.getNickName());
               existingUser.setNickName(user.getNickName());
            }

            if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
               System.out.println("更新头像: " + user.getAvatarUrl());
               existingUser.setAvatarUrl(user.getAvatarUrl());
            }

            existingUser.setUpdateTime(new Date());
            User updatedUser = (User)this.userRepository.save(existingUser);
            System.out.println("========== 用户信息更新成功 ==========");
            return updatedUser;
         }
      } catch (Exception var4) {
         System.out.println("========== 更新错误 ==========");
         System.out.println("错误信息: " + var4.getMessage());
         var4.printStackTrace();
         throw new RuntimeException("更新失败: " + var4.getMessage());
      }
   }

   private String getOpenIdFromWeChat(String code) throws Exception {
      System.out.println("========== 微信验证开始 ==========");
      System.out.println("Code: " + code);
      System.out.println("AppId: " + this.appId);
      String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + this.appId + "&secret=" + this.appSecret + "&js_code=" + code + "&grant_type=authorization_code";
      System.out.println("请求URL: " + url);

      try {
         String response = (String)this.restTemplate.getForObject(url, String.class, new Object[0]);
         System.out.println("微信返回: " + response);
         ObjectMapper mapper = new ObjectMapper();
         Map<String, Object> map = (Map)mapper.readValue(response, Map.class);
         System.out.println("解析后: " + map);
         String openId;
         if (map.containsKey("errcode")) {
            openId = "微信验证失败: " + map.get("errmsg");
            System.out.println("错误: " + openId);
            throw new Exception(openId);
         } else {
            openId = (String)map.get("openid");
            System.out.println("获取的OpenId: " + openId);
            System.out.println("========== 微信验证完成 ==========");
            return openId;
         }
      } catch (Exception var7) {
         System.out.println("微信验证异常: " + var7.getMessage());
         var7.printStackTrace();
         throw var7;
      }
   }

   public User getUserById(Long userId) {
      return (User)this.userRepository.findById(userId).orElse(null);
   }

   public User saveUser(User user) {
      return (User)this.userRepository.save(user);
   }

   @Transactional
   public String createRecharge(Long userId, Integer amount) {
      try {
         System.out.println("========== 创建充值订单开始 ==========");
         System.out.println("用户ID: " + userId);
         System.out.println("充值金额: " + amount);
         User user = (User)this.userRepository.findById(userId).orElse(null);
         if (user == null) {
            throw new Exception("用户不存在");
         } else {
            String outTradeNo = "RCH" + System.currentTimeMillis();
            Recharge recharge = new Recharge();
            recharge.setUserId(userId);
            recharge.setAmount(amount);
            recharge.setOutTradeNo(outTradeNo);
            recharge.setStatus("PENDING");
            this.rechargeRepository.save(recharge);
            System.out.println("========== 充值订单创建成功 ==========");
            System.out.println("商户订单号: " + outTradeNo);
            return outTradeNo;
         }
      } catch (Exception var6) {
         System.out.println("========== 创建充值订单失败 ==========");
         System.out.println("错误信息: " + var6.getMessage());
         var6.printStackTrace();
         throw new RuntimeException("创建充值订单失败: " + var6.getMessage());
      }
   }

   private void processRechargePaid(Recharge recharge, String transactionId) {
      User user = (User)this.userRepository.findById(recharge.getUserId()).orElse(null);
      if (user == null) {
         throw new RuntimeException("用户不存在");
      } else {
         Integer currentPoints = user.getPoints() != null ? user.getPoints() : 0;
         Integer newPoints = currentPoints + recharge.getAmount();
         user.setPoints(newPoints);
         user.setUpdateTime(new Date());
         recharge.setStatus("SUCCESS");
         recharge.setTransactionId(transactionId);
         recharge.setPayTime(new Date());
         this.userRepository.save(user);
         this.rechargeRepository.save(recharge);
         System.out.println("原有积分: " + currentPoints);
         System.out.println("新增积分: " + recharge.getAmount());
         System.out.println("总积分: " + newPoints);
      }
   }

   @Transactional
   public void handleRechargePaid(String outTradeNo, String transactionId) {
      try {
         System.out.println("========== 处理充值支付 ==========");
         System.out.println("商户订单号: " + outTradeNo);
         System.out.println("微信支付单号: " + transactionId);
         Recharge recharge = this.rechargeRepository.findByOutTradeNo(outTradeNo);
         if (recharge == null) {
            throw new Exception("充值订单不存在");
         } else if (!"PENDING".equals(recharge.getStatus())) {
            System.out.println("订单状态异常或已处理，当前状态: " + recharge.getStatus());
         } else {
            this.processRechargePaid(recharge, transactionId);
            System.out.println("========== 处理完成 ==========");
         }
      } catch (Exception var4) {
         System.out.println("========== 处理失败 ==========");
         System.out.println("错误信息: " + var4.getMessage());
         var4.printStackTrace();
         throw new RuntimeException("处理失败: " + var4.getMessage());
      }
   }

   @Transactional
   public void mockPaySuccess(String outTradeNo, String transactionId) {
      try {
         System.out.println("========== 支付成功处理开始 ==========");
         System.out.println("商户订单号: " + outTradeNo);
         System.out.println("微信支付单号: " + transactionId);
         this.handleRechargePaid(outTradeNo, transactionId);
         System.out.println("========== 支付成功处理完成 ==========");
      } catch (Exception var4) {
         System.out.println("========== 支付成功处理失败 ==========");
         System.out.println("错误信息: " + var4.getMessage());
         var4.printStackTrace();
         throw new RuntimeException("支付成功处理失败: " + var4.getMessage());
      }
   }

   /** @deprecated */
   @Deprecated
   public User rechargePoints(Long userId, Integer amount) {
      try {
         System.out.println("========== 充值开始 ==========");
         System.out.println("用户ID: " + userId);
         System.out.println("充值金额: " + amount);
         User user = (User)this.userRepository.findById(userId).orElse(null);
         if (user == null) {
            throw new Exception("用户不存在");
         } else {
            Integer currentPoints = user.getPoints() != null ? user.getPoints() : 0;
            Integer newPoints = currentPoints + amount;
            user.setPoints(newPoints);
            user.setUpdateTime(new Date());
            System.out.println("原有积分: " + currentPoints);
            System.out.println("新增积分: " + amount);
            System.out.println("总积分: " + newPoints);
            User updatedUser = (User)this.userRepository.save(user);
            Recharge recharge = new Recharge();
            recharge.setUserId(userId);
            recharge.setAmount(amount);
            recharge.setStatus("success");
            this.rechargeRepository.save(recharge);
            System.out.println("========== 充值记录已保存 ==========");
            System.out.println("========== 充值成功 ==========");
            return updatedUser;
         }
      } catch (Exception var8) {
         System.out.println("========== 充值失败 ==========");
         System.out.println("错误信息: " + var8.getMessage());
         var8.printStackTrace();
         throw new RuntimeException("充值失败: " + var8.getMessage());
      }
   }

   public List<Recharge> getRechargeHistory(Long userId) {
      try {
         System.out.println("========== 获取充值历史 ==========");
         System.out.println("用户ID: " + userId);
         List<Recharge> rechargeList = this.rechargeRepository.findByUserIdOrderByCreateTimeDesc(userId);
         System.out.println("充值记录数: " + rechargeList.size());
         return rechargeList;
      } catch (Exception var3) {
         System.out.println("========== 获取充值历史失败 ==========");
         System.out.println("错误信息: " + var3.getMessage());
         var3.printStackTrace();
         throw new RuntimeException("获取充值历史失败: " + var3.getMessage());
      }
   }
   public UserProfileResponse getProfile(Long userId) {
  User user = this.userRepository.findById(userId).orElse(null);
  if (user == null) throw new RuntimeException("用户不存在");

  UserProfileResponse resp = new UserProfileResponse();
  resp.setUserId(user.getId());
  resp.setNickname(user.getNickName());
  resp.setPhoneNumber(user.getPhoneNumber());
  resp.setShippingAddress(user.getShippingAddress());

  if (user.getBirthday() != null) {
    resp.setBirthday(DATE_FMT.format(user.getBirthday()));
  } else {
    resp.setBirthday(null);
  }
  return resp;
}

@Transactional
public UserProfileResponse updateProfile(UpdateUserProfileRequest req) {
  if (req.getUserId() == null) throw new RuntimeException("用户ID不能为空");

  String nickname = req.getNickname() == null ? "" : req.getNickname().trim();
  if (nickname.isEmpty()) throw new RuntimeException("昵称不能为空");
  if (nickname.length() < 1 || nickname.length() > 12)
    throw new RuntimeException("昵称长度需 1-12 字");

  String phone = req.getPhoneNumber() == null ? "" : req.getPhoneNumber().trim();
  if (!phone.isEmpty() && !CN_MOBILE.matcher(phone).matches())
    throw new RuntimeException("手机号格式不正确");

  java.util.Date birthday = null;
  String birthdayStr = req.getBirthday() == null ? "" : req.getBirthday().trim();
  if (!birthdayStr.isEmpty()) {
    try {
      birthday = DATE_FMT.parse(birthdayStr);
    } catch (ParseException e) {
      throw new RuntimeException("生日格式不正确，应为 yyyy-MM-dd");
    }
  }

  User user = this.userRepository.findById(req.getUserId()).orElse(null);
  if (user == null) throw new RuntimeException("用户不存在");

  user.setNickName(nickname);
  user.setPhoneNumber(phone.isEmpty() ? null : phone);
  user.setBirthday(birthday);

  String addr = req.getShippingAddress() == null ? null : req.getShippingAddress().trim();
  user.setShippingAddress((addr == null || addr.isEmpty()) ? null : addr);

  user.setUpdateTime(new Date());
  this.userRepository.save(user);

  return getProfile(user.getId());
}
}
