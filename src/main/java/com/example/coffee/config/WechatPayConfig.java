package com.example.coffee.config;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.DefaultHttpClientBuilder;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceImpl;
import com.wechat.pay.java.service.payments.model.Transaction;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;

/**
 * 微信支付 V3 配置类
 * 基于官方 wechatpay-apache-httpclient SDK 初始化 HTTP 客户端和相关服务
 */
@Component
public class WechatPayConfig {

    @Autowired
    private WechatPayProperties wechatPayProperties;

    /**
     * 初始化微信支付 Config 对象
     * 该对象包含了 appid、mchid、apiV3Key、serialNo 以及私钥等信息
     */
    @Bean
    public Config wechatPayCoreConfig() throws Exception {
        // 从配置文件读取私钥
        String privateKeyContent = readPrivateKeyContent(wechatPayProperties.getPrivateKeyPath());

        // 创建 Config 对象
        Config config = new Config.Builder()
                .merchantId(wechatPayProperties.getMchId())
                .privateKeyFromContent(privateKeyContent)
                .merchantSerialNumber(wechatPayProperties.getSerialNo())
                .apiV3Key(wechatPayProperties.getApiV3Key())
                .build();

        return config;
    }

    /**
     * 初始化 JsapiService，用于调用微信支付 JSAPI 相关接口
     */
    @Bean
    public JsapiServiceImpl jsapiService(Config config) {
        return new JsapiServiceImpl.Builder()
                .config(config)
                .build();
    }

    /**
     * 从 PEM 文件读取私钥内容（保留 BEGIN 和 END 标签）
     */
    private String readPrivateKeyContent(String filePath) throws Exception {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
}

