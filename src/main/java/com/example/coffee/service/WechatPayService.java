package com.example.coffee.service;

import com.example.coffee.config.WechatPayProperties;
import com.example.coffee.util.SignatureUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WechatPayService {
   @Autowired
   private WechatPayProperties wechatPayProperties;
   @Autowired
   private HttpClient wechatPayHttpClient;
   @Autowired
   private ObjectMapper objectMapper;
   @Autowired
   private PrivateKey wechatPayPrivateKey;
   @Autowired
   private UserService userService;
   private static final String JSAPI_PREPAY_URL = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";

   public WechatPayService.JsapiPayResponse createRechargeAndPrepay(Long userId, Integer amount, String openId) {
      try {
         System.out.println("========== 创建充值订单并预下单开始 ==========");
         System.out.println("用户ID: " + userId);
         System.out.println("充值金额: " + amount);
         System.out.println("用户openId: " + openId);
         String outTradeNo = this.userService.createRecharge(userId, amount);
         System.out.println("创建的订单号: " + outTradeNo);
         String prepayId = this.callJsapiPrepay(outTradeNo, "账户充值", amount * 100, openId);
         System.out.println("获取的 prepay_id: " + prepayId);
         Map<String, String> payParams = this.generatePayParams(prepayId);
         System.out.println("========== 创建充值订单并预下单成功 ==========");
         return new WechatPayService.JsapiPayResponse(outTradeNo, this.wechatPayProperties.getAppId(), (String)payParams.get("timeStamp"), (String)payParams.get("nonceStr"), (String)payParams.get("package"), (String)payParams.get("signType"), (String)payParams.get("paySign"));
      } catch (Exception var7) {
         System.out.println("========== 创建充值订单并预下单失败 ==========");
         System.out.println("错误信息: " + var7.getMessage());
         var7.printStackTrace();
         throw new RuntimeException("预下单失败: " + var7.getMessage());
      }
   }

   private String callJsapiPrepay(String outTradeNo, String description, Integer amountCents, String openId) throws Exception {
      System.out.println("========== 调用 JSAPI 统一下单接口 ==========");
      Map<String, Object> requestBody = new HashMap();
      requestBody.put("appid", this.wechatPayProperties.getAppId());
      requestBody.put("mchid", this.wechatPayProperties.getMchId());
      requestBody.put("description", description);
      requestBody.put("out_trade_no", outTradeNo);
      requestBody.put("notify_url", this.wechatPayProperties.getNotifyUrl());
      Map<String, Integer> amount = new HashMap();
      amount.put("total", amountCents);
      requestBody.put("amount", amount);
      Map<String, String> payer = new HashMap();
      payer.put("openid", openId);
      requestBody.put("payer", payer);
      String requestBodyJson = this.objectMapper.writeValueAsString(requestBody);
      System.out.println("请求体: " + requestBodyJson);
      HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
      httpPost.addHeader("Accept", "application/json");
      httpPost.addHeader("Content-type", "application/json; charset=utf-8");
      httpPost.setEntity(new StringEntity(requestBodyJson, StandardCharsets.UTF_8));
      HttpResponse response = this.wechatPayHttpClient.execute(httpPost);
      HttpEntity entity = response.getEntity();
      String responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
      System.out.println("响应状态码: " + response.getStatusLine().getStatusCode());
      System.out.println("响应体: " + responseBody);
      Map<String, Object> responseMap = (Map)this.objectMapper.readValue(responseBody, Map.class);
      String prepayId;
      if (response.getStatusLine().getStatusCode() != 200) {
         prepayId = (String)responseMap.get("code");
         String errMsg = (String)responseMap.get("message");
         throw new Exception("统一下单失败: " + prepayId + " - " + errMsg);
      } else {
         prepayId = (String)responseMap.get("prepay_id");
         if (prepayId != null && !prepayId.isEmpty()) {
            return prepayId;
         } else {
            throw new Exception("响应中缺少 prepay_id");
         }
      }
   }

   private Map<String, String> generatePayParams(String prepayId) throws Exception {
      Map<String, String> params = new HashMap();
      String timeStamp = String.valueOf(System.currentTimeMillis() / 1000L);
      params.put("timeStamp", timeStamp);
      String nonceStr = this.generateNonceStr();
      params.put("nonceStr", nonceStr);
      String packageStr = "prepay_id=" + prepayId;
      params.put("package", packageStr);
      params.put("signType", "RSA");
      String paySign = this.generatePaySign(timeStamp, nonceStr, packageStr);
      params.put("paySign", paySign);
      System.out.println("生成的支付参数:");
      System.out.println("  timeStamp: " + timeStamp);
      System.out.println("  nonceStr: " + nonceStr);
      System.out.println("  package: " + packageStr);
      System.out.println("  signType: RSA");
      System.out.println("  paySign: " + paySign);
      return params;
   }

   private String generatePaySign(String timeStamp, String nonceStr, String packageStr) throws Exception {
      String signContent = this.wechatPayProperties.getAppId() + "\n" + timeStamp + "\n" + nonceStr + "\n" + packageStr + "\n";
      System.out.println("待签名内容: " + signContent.replace("\n", "\\n"));
      String paySign = SignatureUtils.sign(signContent, this.wechatPayPrivateKey);
      System.out.println("生成的 paySign: " + paySign);
      return paySign;
   }

   private String generateNonceStr() {
      String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < 32; ++i) {
         int index = (int)(Math.random() * (double)characters.length());
         sb.append(characters.charAt(index));
      }

      return sb.toString();
   }

   public static class JsapiPayResponse {
      public String outTradeNo;
      public String appId;
      public String timeStamp;
      public String nonceStr;
      public String packageVal;
      public String signType;
      public String paySign;

      public JsapiPayResponse(String outTradeNo, String appId, String timeStamp, String nonceStr, String packageVal, String signType, String paySign) {
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
