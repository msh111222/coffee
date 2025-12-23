package com.example.coffee.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import java.io.FileInputStream;
import java.security.PrivateKey;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WechatPayConfig {
   @Autowired
   private WechatPayProperties wechatPayProperties;

   @Bean
   public PrivateKey wechatPayPrivateKey() throws Exception {
      System.out.println("========== 加载商户私钥 ==========");
      System.out.println("私钥路径: " + this.wechatPayProperties.getPrivateKeyPath());

      try {
         FileInputStream fis = new FileInputStream(this.wechatPayProperties.getPrivateKeyPath());

         PrivateKey var3;
         try {
            PrivateKey privateKey = PemUtil.loadPrivateKey(fis);
            System.out.println("私钥加载成功");
            var3 = privateKey;
         } catch (Throwable var5) {
            try {
               fis.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }

            throw var5;
         }

         fis.close();
         return var3;
      } catch (Exception var6) {
         System.out.println("私钥加载失败: " + var6.getMessage());
         throw var6;
      }
   }

   @Bean
   public AutoUpdateCertificatesVerifier wechatPayCertificatesVerifier(PrivateKey privateKey) throws Exception {
      System.out.println("========== 初始化平台证书验证器 ==========");
      AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(new WechatPay2Credentials(this.wechatPayProperties.getMchId(), new PrivateKeySigner(this.wechatPayProperties.getSerialNo(), privateKey)), this.wechatPayProperties.getApiV3Key().getBytes());
      System.out.println("平台证书验证器初始化成功");
      return verifier;
   }

   @Bean
   public HttpClient wechatPayHttpClient(PrivateKey privateKey, AutoUpdateCertificatesVerifier verifier) throws Exception {
      System.out.println("========== 创建微信支付 HttpClient ==========");
      HttpClient httpClient = WechatPayHttpClientBuilder.create().withMerchant(this.wechatPayProperties.getMchId(), this.wechatPayProperties.getSerialNo(), privateKey).withValidator((response) -> {
         return true;
      }).build();
      System.out.println("微信支付 HttpClient 创建成功");
      return httpClient;
   }

   @Bean
   public ObjectMapper objectMapper() {
      return new ObjectMapper();
   }
}
