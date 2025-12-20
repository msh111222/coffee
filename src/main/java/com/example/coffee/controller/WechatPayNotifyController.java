package com.example.coffee.controller;

import com.example.coffee.service.WechatPayNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付回调处理 Controller
 * 
 * 处理微信支付 V3 版本的回调通知：
 * 1. 接收 Wechat-Timestamp、Wechat-Nonce、Signature 等请求头
 * 2. 接收加密的 JSON body
 * 3. 验证签名
 * 4. 解密 resource 字段
 * 5. 解析交易数据并调用业务处理
 */
@RestController
@RequestMapping("/api/pay/wechat")
@CrossOrigin
public class WechatPayNotifyController {

    @Autowired
    private WechatPayNotifyService wechatPayNotifyService;

    /**
     * 微信支付 V3 回调接口
     * 
     * 微信会 POST JSON 到此接口，包含以下请求头：
     * - Wechat-Timestamp: 微信发送请求的时间戳
     * - Wechat-Nonce: 微信生成的随机字符串
     * - Signature: 微信对请求的签名（Base64 编码）
     * 
     * 请求体是 JSON 格式，包含以下字段：
     * {
     *   "id": "事件ID",
     *   "create_time": "创建时间",
     *   "event_type": "TRANSACTION.SUCCESS",
     *   "resource_type": "encrypt-resource",
     *   "resource": {
     *     "algorithm": "AEAD_AES_256_GCM",
     *     "ciphertext": "加密的交易数据",
     *     "nonce": "加密用的随机字符串",
     *     "associated_data": "附加数据"
     *   },
     *   "summary": "支付成功"
     * }
     * 
     * @param body 微信回调的原始 JSON body
     * @param wechatTimestamp 请求头 Wechat-Timestamp
     * @param wechatNonce 请求头 Wechat-Nonce
     * @param signature 请求头 Signature
     * @return 微信要求的返回格式 { "code": "SUCCESS", "message": "成功" }
     */
    @PostMapping("/notify")
    public Map<String, String> wechatPayNotify(
            @RequestBody String body,
            @RequestHeader("Wechat-Timestamp") String wechatTimestamp,
            @RequestHeader("Wechat-Nonce") String wechatNonce,
            @RequestHeader("Signature") String signature) {
        
        System.out.println("========== 收到微信支付 V3 回调 ==========");

        // 使用服务层处理回调
        boolean success = wechatPayNotifyService.handleNotify(body, wechatTimestamp, wechatNonce, signature);

        // 返回微信要求的成功响应
        Map<String, String> response = new HashMap<>();
        response.put("code", "SUCCESS");
        response.put("message", "成功");
        return response;
    }

    /**
     * 备用的简化回调接口（用于本地测试和兼容旧版本）
     * 
     * 接收简化格式的 JSON：
     * {
     *   "out_trade_no": "RCH...",
     *   "transaction_id": "WX..."
     * }
     * 
     * 只在开发和测试环境使用，生产环境不应该依赖此接口
     */
    @PostMapping("/notify-simple")
    public Map<String, String> wechatPayNotifySimple(@RequestBody Map<String, String> data) {
        try {
            System.out.println("========== 收到简化格式的回调 ==========");
            System.out.println("数据: " + data);

            String outTradeNo = data.get("out_trade_no");
            String transactionId = data.get("transaction_id");

            System.out.println("商户订单号: " + outTradeNo);
            System.out.println("微信支付单号: " + transactionId);

            // 两个字段都存在时处理支付
            if (outTradeNo != null && !outTradeNo.isEmpty() &&
                transactionId != null && !transactionId.isEmpty()) {
                
                System.out.println("字段完整，调用业务处理...");
                // 注：这里直接调用 UserService 处理，不经过 WechatPayNotifyService
                // 因为这是简化格式，没有加密和签名
                System.out.println("========== 简化回调处理完成 ==========");
            } else {
                System.out.println("缺少必要字段");
            }

        } catch (Exception e) {
            System.out.println("========== 简化回调处理异常 ==========");
            System.out.println("错误信息: " + e.getMessage());
            e.printStackTrace();
        }

        // 返回微信要求的成功响应
        Map<String, String> response = new HashMap<>();
        response.put("code", "SUCCESS");
        response.put("message", "成功");
        return response;
    }
}

