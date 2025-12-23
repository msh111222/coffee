package com.example.coffee.controller;

import com.example.coffee.common.Result;
import com.example.coffee.service.WechatPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/pay"})
@CrossOrigin
public class PayController {
   @Autowired
   private WechatPayService wechatPayService;

   @PostMapping({"/createRechargeAndPrepay"})
   public Result createRechargeAndPrepay(@RequestBody PayController.CreateRechargeAndPrepayRequest request) {
      try {
         System.out.println("========== 创建充值订单并预下单开始 ==========");
         if (request.getUserId() == null) {
            return Result.error("用户ID不能为空");
         } else if (request.getAmount() != null && request.getAmount() > 0) {
            if (request.getOpenId() != null && !request.getOpenId().isEmpty()) {
               System.out.println("用户ID: " + request.getUserId());
               System.out.println("充值金额: " + request.getAmount());
               System.out.println("用户openId: " + request.getOpenId());
               WechatPayService.JsapiPayResponse payResponse = this.wechatPayService.createRechargeAndPrepay(request.getUserId(), request.getAmount(), request.getOpenId());
               System.out.println("========== 创建充值订单并预下单成功 ==========");
               return Result.success("预下单成功", new PayController.CreateRechargeAndPrepayResponse(payResponse.getOutTradeNo(), payResponse.getAppId(), payResponse.getTimeStamp(), payResponse.getNonceStr(), payResponse.getPackageVal(), payResponse.getSignType(), payResponse.getPaySign()));
            } else {
               return Result.error("openId不能为空");
            }
         } else {
            return Result.error("充值金额必须大于0");
         }
      } catch (Exception var3) {
         System.out.println("========== 创建充值订单并预下单失败 ==========");
         System.out.println("错误信息: " + var3.getMessage());
         var3.printStackTrace();
         return Result.error("预下单失败: " + var3.getMessage());
      }
   }

   public static class CreateRechargeAndPrepayRequest {
      private Long userId;
      private Integer amount;
      private String openId;

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

      public String getOpenId() {
         return this.openId;
      }

      public void setOpenId(String openId) {
         this.openId = openId;
      }
   }

   public static class CreateRechargeAndPrepayResponse {
      private String outTradeNo;
      private String appId;
      private String timeStamp;
      private String nonceStr;
      private String packageVal;
      private String signType;
      private String paySign;

      public CreateRechargeAndPrepayResponse(String outTradeNo, String appId, String timeStamp, String nonceStr, String packageVal, String signType, String paySign) {
         this.outTradeNo = outTradeNo;
         this.appId = appId;
         this.timeStamp = timeStamp;
         this.nonceStr = nonceStr;
         this.packageVal = packageVal;
         this.signType = signType;
         this.paySign = paySign;
      }

      public String getOutTradeNo() {
         return this.outTradeNo;
      }

      public void setOutTradeNo(String outTradeNo) {
         this.outTradeNo = outTradeNo;
      }

      public String getAppId() {
         return this.appId;
      }

      public void setAppId(String appId) {
         this.appId = appId;
      }

      public String getTimeStamp() {
         return this.timeStamp;
      }

      public void setTimeStamp(String timeStamp) {
         this.timeStamp = timeStamp;
      }

      public String getNonceStr() {
         return this.nonceStr;
      }

      public void setNonceStr(String nonceStr) {
         this.nonceStr = nonceStr;
      }

      public String getPackageVal() {
         return this.packageVal;
      }

      public void setPackageVal(String packageVal) {
         this.packageVal = packageVal;
      }

      public String getSignType() {
         return this.signType;
      }

      public void setSignType(String signType) {
         this.signType = signType;
      }

      public String getPaySign() {
         return this.paySign;
      }

      public void setPaySign(String paySign) {
         this.paySign = paySign;
      }
   }
}
