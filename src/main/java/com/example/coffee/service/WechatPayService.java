package com.example.coffee.service;

import com.example.coffee.config.WechatPayProperties;
import com.example.coffee.util.SignatureUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceImpl;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付服务类
 * 负责与微信支付 V3 接口交互，包括：
 * - JSAPI 统一下单
 * - 生成前端调用所需的 paySign
 */
@Service
public class WechatPayService {

    @Autowired
    private JsapiServiceImpl jsapiService;

    @Autowired
    private Config wechatPayCoreConfig;

    @Autowired
    private WechatPayProperties wechatPayProperties;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 调用微信支付 JSAPI 统一下单接口
     * 
     * @param outTradeNo 商户订单号
     * @param description 商品描述
     * @param amount 金额（单位：分）
     * @param openId 用户 openId
     * @return PrepayResponse 包含前端所需的所有参数
     */
    public PrepayResponse prepay(String outTradeNo, String description, Integer amount, String openId) {
        try {
            System.out.println("========== 微信支付 JSAPI 统一下单开始 ==========");
            System.out.println("商户订单号: " + outTradeNo);
            System.out.println("商品描述: " + description);
            System.out.println("金额(分): " + amount);
            System.out.println("用户 openId: " + openId);

            // 1. 构建请求
            PrepayRequest request = new PrepayRequest();
            request.setAppid(wechatPayProperties.getAppId());
            request.setMchid(wechatPayProperties.getMchId());
            request.setDescription(description);
            request.setNotifyUrl(wechatPayProperties.getNotifyUrl());
            request.setOutTradeNo(outTradeNo);

            // 金额设置
            Amount amountObj = new Amount();
            amountObj.setTotal(amount);
            request.setAmount(amountObj);

            // 用户信息
            Payer payer = new Payer();
            payer.setOpenid(openId);
            request.setPayer(payer);

            // 2. 调用微信支付接口
            PrepayWithRequestPaymentResponse response = jsapiService.prepayWithRequestPayment(request);
            System.out.println("微信返回的 prepay_id: " + response.getPrepayId());

            // 3. 生成前端调用所需的参数
            Map<String, String> payParams = generatePayParams(
                    response.getPrepayId(),
                    outTradeNo
            );

            System.out.println("========== 微信支付 JSAPI 统一下单成功 ==========");

            return new PrepayResponse(
                    wechatPayProperties.getAppId(),
                    outTradeNo,
                    payParams.get("timeStamp"),
                    payParams.get("nonceStr"),
                    payParams.get("package"),
                    payParams.get("signType"),
                    payParams.get("paySign")
            );

        } catch (Exception e) {
            System.out.println("========== 微信支付 JSAPI 统一下单失败 ==========");
            System.out.println("错误信息: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("统一下单失败: " + e.getMessage());
        }
    }

    /**
     * 生成前端调用 wx.requestPayment 所需的参数
     * 
     * @param prepayId 微信返回的 prepay_id
     * @param outTradeNo 商户订单号
     * @return 包含 timeStamp, nonceStr, package, signType, paySign 的 Map
     */
    private Map<String, String> generatePayParams(String prepayId, String outTradeNo) throws Exception {
        Map<String, String> result = new HashMap<>();

        // 时间戳（秒级，String 格式）
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        result.put("timeStamp", timeStamp);

        // 随机字符串
        String nonceStr = generateNonceStr();
        result.put("nonceStr", nonceStr);

        // package 字段
        String packageStr = "prepay_id=" + prepayId;
        result.put("package", packageStr);

        // signType 固定为 RSA
        String signType = "RSA";
        result.put("signType", signType);

        // 生成 paySign
        String paySign = generatePaySign(
                wechatPayProperties.getAppId(),
                timeStamp,
                nonceStr,
                packageStr
        );
        result.put("paySign", paySign);

        System.out.println("生成的支付参数:");
        System.out.println("  timeStamp: " + timeStamp);
        System.out.println("  nonceStr: " + nonceStr);
        System.out.println("  package: " + packageStr);
        System.out.println("  signType: " + signType);
        System.out.println("  paySign: " + paySign);

        return result;
    }

    /**
     * 生成 paySign 签名
     * 签名内容 = appid\ntimestamp\nnoncestr\nprepay_id=xxxxx\n
     * 使用商户私钥进行 RSA-SHA256 签名
     */
    private String generatePaySign(String appId, String timeStamp, String nonceStr, String packageStr) throws Exception {
        // 待签名的内容
        String signContent = appId + "\n" + timeStamp + "\n" + nonceStr + "\n" + packageStr + "\n";
        System.out.println("待签名内容: " + signContent.replace("\n", "\\n"));

        // 从私钥文件读取私钥
        PrivateKey privateKey = SignatureUtils.getPrivateKeyFromPem(wechatPayProperties.getPrivateKeyPath());

        // 签名
        String paySign = SignatureUtils.sign(signContent, privateKey);
        System.out.println("生成的 paySign: " + paySign);

        return paySign;
    }

    /**
     * 生成随机字符串（用于 nonceStr）
     */
    private String generateNonceStr() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            int index = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 预付款响应对象
     * 包含前端调用 wx.requestPayment 所需的所有参数
     */
    public static class PrepayResponse {
        public String appId;
        public String outTradeNo;
        public String timeStamp;
        public String nonceStr;
        public String packageVal;  // "prepay_id=xxx"
        public String signType;    // "RSA"
        public String paySign;

        public PrepayResponse(String appId, String outTradeNo, String timeStamp, 
                            String nonceStr, String packageVal, String signType, String paySign) {
            this.appId = appId;
            this.outTradeNo = outTradeNo;
            this.timeStamp = timeStamp;
            this.nonceStr = nonceStr;
            this.packageVal = packageVal;
            this.signType = signType;
            this.paySign = paySign;
        }

        // Getter/Setter
        public String getAppId() { return appId; }
        public void setAppId(String appId) { this.appId = appId; }

        public String getOutTradeNo() { return outTradeNo; }
        public void setOutTradeNo(String outTradeNo) { this.outTradeNo = outTradeNo; }

        public String getTimeStamp() { return timeStamp; }
        public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

        public String getNonceStr() { return nonceStr; }
        public void setNonceStr(String nonceStr) { this.nonceStr = nonceStr; }

        public String getPackageVal() { return packageVal; }
        public void setPackageVal(String packageVal) { this.packageVal = packageVal; }

        public String getSignType() { return signType; }
        public void setSignType(String signType) { this.signType = signType; }

        public String getPaySign() { return paySign; }
        public void setPaySign(String paySign) { this.paySign = paySign; }
    }
}

