package com.example.coffee.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信支付配置属性
 * 从 application.properties 中读取 wechat.* 配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatPayProperties {
    
    /**
     * 小程序 AppID
     */
    private String appId;
    
    /**
     * 小程序 AppSecret
     */
    private String appSecret;
    
    /**
     * 微信支付 - 商户号
     */
    private String mchId;
    
    /**
     * 微信支付 - API v3 密钥
     */
    private String apiV3Key;
    
    /**
     * 微信支付 - 商户 API 证书序列号
     */
    private String serialNo;
    
    /**
     * 微信支付 - 商户私钥文件路径
     */
    private String privateKeyPath;
    
    /**
     * 微信支付 - 回调通知 URL
     */
    private String notifyUrl;
}

