package com.example.coffee.controller;

import com.example.coffee.common.Result;
import com.example.coffee.service.UserService;
import com.example.coffee.service.WechatPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 微信支付控制器
 * 处理与微信支付相关的接口（除了回调通知外）
 */
@RestController
@RequestMapping("/api/pay")
@CrossOrigin
public class PayController {

    @Autowired
    private UserService userService;

    @Autowired
    private WechatPayService wechatPayService;

    /**
     * 创建充值订单并获取 JSAPI 支付参数
     * 
     * 流程：
     * 1. 在数据库创建 Recharge 记录（状态为 PENDING）
     * 2. 调用微信支付 JSAPI 统一下单接口
     * 3. 生成前端所需的支付参数（包括 paySign）
     * 4. 返回给前端
     * 
     * @param request 包含 userId、amount、openId 等信息
     * @return 包含 prepay_id、paySign 等参数的响应
     */
    @PostMapping("/createRechargeAndPrepay")
    public Result createRechargeAndPrepay(@RequestBody CreateRechargeAndPrepayRequest request) {
        try {
            System.out.println("========== 创建充值订单并预下单开始 ==========");

            // 1. 参数验证
            if (request.getUserId() == null) {
                return Result.error("用户ID不能为空");
            }
            if (request.getAmount() == null || request.getAmount() <= 0) {
                return Result.error("充值金额必须大于0");
            }
            if (request.getOpenId() == null || request.getOpenId().isEmpty()) {
                return Result.error("openId不能为空");
            }

            System.out.println("用户ID: " + request.getUserId());
            System.out.println("充值金额: " + request.getAmount());
            System.out.println("用户openId: " + request.getOpenId());

            // 2. 创建充值记录（返回 outTradeNo）
            String outTradeNo = userService.createRecharge(request.getUserId(), request.getAmount());
            System.out.println("创建的订单号: " + outTradeNo);

            // 3. 调用微信支付 JSAPI 统一下单
            // 注：金额需要转换为分 (1元=100分)
            Integer amountInCents = request.getAmount() * 100;
            WechatPayService.PrepayResponse prepayResponse = wechatPayService.prepay(
                    outTradeNo,
                    "账户充值", // 商品描述
                    amountInCents,
                    request.getOpenId()
            );

            System.out.println("========== 创建充值订单并预下单成功 ==========");

            // 4. 返回结果
            return Result.success("预下单成功", new CreateRechargeAndPrepayResponse(
                    outTradeNo,
                    prepayResponse.getAppId(),
                    prepayResponse.getTimeStamp(),
                    prepayResponse.getNonceStr(),
                    prepayResponse.getPackageVal(),
                    prepayResponse.getSignType(),
                    prepayResponse.getPaySign()
            ));

        } catch (Exception e) {
            System.out.println("========== 创建充值订单并预下单失败 ==========");
            System.out.println("错误信息: " + e.getMessage());
            e.printStackTrace();
            return Result.error("预下单失败: " + e.getMessage());
        }
    }

    /**
     * 创建充值并预下单的请求 DTO
     */
    public static class CreateRechargeAndPrepayRequest {
        private Long userId;
        private Integer amount;    // 金额（单位：元）
        private String openId;     // 用户小程序 openId

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }
    }

    /**
     * 创建充值并预下单的响应 DTO
     */
    public static class CreateRechargeAndPrepayResponse {
        private String outTradeNo;   // 商户订单号
        private String appId;        // 小程序 AppID
        private String timeStamp;    // 时间戳（秒）
        private String nonceStr;     // 随机字符串
        private String packageVal;   // "prepay_id=xxx"
        private String signType;     // "RSA"
        private String paySign;      // 签名

        public CreateRechargeAndPrepayResponse(String outTradeNo, String appId, String timeStamp,
                                             String nonceStr, String packageVal, String signType, String paySign) {
            this.outTradeNo = outTradeNo;
            this.appId = appId;
            this.timeStamp = timeStamp;
            this.nonceStr = nonceStr;
            this.packageVal = packageVal;
            this.signType = signType;
            this.paySign = paySign;
        }

        // Getter/Setter
        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public void setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
        }

        public String getPackageVal() {
            return packageVal;
        }

        public void setPackageVal(String packageVal) {
            this.packageVal = packageVal;
        }

        public String getSignType() {
            return signType;
        }

        public void setSignType(String signType) {
            this.signType = signType;
        }

        public String getPaySign() {
            return paySign;
        }

        public void setPaySign(String paySign) {
            this.paySign = paySign;
        }
    }
}

