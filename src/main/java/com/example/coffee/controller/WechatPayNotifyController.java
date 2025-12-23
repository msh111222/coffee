package com.example.coffee.controller;

import com.example.coffee.config.WechatPayProperties;
import com.example.coffee.service.UserService;
import com.example.coffee.util.AesGcmUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/pay/wechat"})
@CrossOrigin
public class WechatPayNotifyController {
   @Autowired
   private UserService userService;
   @Autowired
   private WechatPayProperties wechatPayProperties;
   @Autowired
   private ObjectMapper objectMapper;

   @PostMapping({"/notify"})
   public Map<String, String> wechatPayNotify(HttpServletRequest request) {
      try {
         System.out.println("========== 收到微信支付 V3 回调 ==========");
         String wechatTimestamp = request.getHeader("Wechatpay-Timestamp");
         String wechatNonce = request.getHeader("Wechatpay-Nonce");
         String wechatSignature = request.getHeader("Wechatpay-Signature");
         String wechatSerial = request.getHeader("Wechatpay-Serial");
         System.out.println("Wechatpay-Timestamp: " + wechatTimestamp);
         System.out.println("Wechatpay-Nonce: " + wechatNonce);
         System.out.println("Wechatpay-Signature: " + wechatSignature);
         System.out.println("Wechatpay-Serial: " + wechatSerial);
         String requestBody = this.readRequestBody(request);
         System.out.println("请求体: " + requestBody);
         this.processNotify(requestBody);
         Map<String, String> response = new HashMap();
         response.put("code", "SUCCESS");
         response.put("message", "成功");
         return response;
      } catch (Exception var8) {
         System.out.println("========== 回调处理异常 ==========");
         System.out.println("错误信息: " + var8.getMessage());
         var8.printStackTrace();
         Map<String, String> response = new HashMap();
         response.put("code", "FAIL");
         response.put("message", "处理失败");
         return response;
      }
   }

   private void processNotify(String requestBody) throws Exception {
      System.out.println("========== 处理微信回调 ==========");
      Map<String, Object> notifyData = (Map)this.objectMapper.readValue(requestBody, Map.class);
      String eventType = (String)notifyData.get("event_type");
      System.out.println("事件类型: " + eventType);
      if (!"TRANSACTION.SUCCESS".equals(eventType)) {
         System.out.println("不是交易成功事件，忽略处理");
      } else {
         Map<String, Object> resource = (Map)notifyData.get("resource");
         String ciphertext = (String)resource.get("ciphertext");
         String nonce = (String)resource.get("nonce");
         String associatedData = (String)resource.get("associated_data");
         System.out.println("开始解密 resource...");
         String plaintext = AesGcmUtils.decrypt(this.wechatPayProperties.getApiV3Key(), ciphertext, associatedData, nonce);
         System.out.println("解密后的数据: " + plaintext);
         Map<String, Object> tradeData = (Map)this.objectMapper.readValue(plaintext, Map.class);
         String outTradeNo = (String)tradeData.get("out_trade_no");
         String transactionId = (String)tradeData.get("transaction_id");
         String tradeState = (String)tradeData.get("trade_state");
         System.out.println("商户订单号: " + outTradeNo);
         System.out.println("微信支付单号: " + transactionId);
         System.out.println("交易状态: " + tradeState);
         if ("SUCCESS".equals(tradeState)) {
            System.out.println("交易成功，调用业务处理...");
            this.userService.handleRechargePaid(outTradeNo, transactionId);
            System.out.println("业务处理完成");
         } else if ("REFUND".equals(tradeState)) {
            System.out.println("交易已退款，仅记录日志");
         } else if ("CLOSED".equals(tradeState)) {
            System.out.println("交易已关闭，仅记录日志");
         } else {
            System.out.println("未知的交易状态: " + tradeState);
         }

         System.out.println("========== 微信支付回调处理完成 ==========");
      }
   }

   private String readRequestBody(HttpServletRequest request) throws Exception {
      StringBuilder sb = new StringBuilder();
      BufferedReader reader = request.getReader();

      String line;
      try {
         while((line = reader.readLine()) != null) {
            sb.append(line);
         }
      } catch (Throwable var7) {
         if (reader != null) {
            try {
               reader.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (reader != null) {
         reader.close();
      }

      return sb.toString();
   }
}
