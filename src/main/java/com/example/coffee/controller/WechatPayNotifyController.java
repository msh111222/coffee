package com.example.coffee.controller;

import com.example.coffee.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付回调处理 Controller
 * 
 * TODO: 后续需要改成解析微信支付 V3 的正式回调结构：
 *   - 解密 resource 字段
 *   - 验证签名（Wechat-Timestamp + Wechat-Nonce + Signature）
 *   - 解析真实的支付回调数据结构
 */
@RestController
@RequestMapping("/api/pay/wechat")
@CrossOrigin
public class WechatPayNotifyController {

    @Autowired
    private UserService userService;

    /**
     * 微信支付回调接口（占位实现，便于本地测试）
     * 
     * 正式环境中，微信会 POST JSON 到此接口。
     * 目前接收结构示例：{ "out_trade_no": "RCH123456", "transaction_id": "WX789" }
     * 
     * @param body 微信回调的原始 JSON body
     * @return 微信要求的返回格式
     */
    @PostMapping("/notify")
    public Map<String, String> wechatPayNotify(@RequestBody String body) {
        try {
            System.out.println("========== 收到微信支付回调 ==========");
            System.out.println("原始body: " + body);

            // 使用 Jackson 解析 JSON
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> data = mapper.readValue(body, Map.class);

            // 提取 out_trade_no 和 transaction_id
            String outTradeNo = (String) data.get("out_trade_no");
            String transactionId = (String) data.get("transaction_id");

            System.out.println("商户订单号: " + outTradeNo);
            System.out.println("微信支付单号: " + transactionId);

            // 两个字段都存在时处理支付
            if (outTradeNo != null && !outTradeNo.isEmpty() &&
                transactionId != null && !transactionId.isEmpty()) {
                
                System.out.println("字段完整，调用业务处理...");
                userService.handleRechargePaid(outTradeNo, transactionId);
                System.out.println("========== 回调处理完成 ==========");
            } else {
                System.out.println("缺少必要字段，out_trade_no=" + outTradeNo + 
                                 ", transaction_id=" + transactionId);
            }

        } catch (Exception e) {
            System.out.println("========== 回调处理异常 ==========");
            System.out.println("错误信息: " + e.getMessage());
            e.printStackTrace();
        }

        // 返回微信要求的成功响应
        Map<String, String> response = new HashMap<>();
        response.put("code", "SUCCESS");
        response.put("message", "OK");
        return response;
    }
}

