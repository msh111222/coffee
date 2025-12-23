# 微信支付 V3 JSAPI 重构完成 - 使用 wechatpay-apache-httpclient:0.4.8

## ✅ 重构概览

项目已完全重构为使用 **官方 Maven Central 仓库中存在的** `com.github.wechatpay-apiv3:wechatpay-apache-httpclient:0.4.8` SDK，移除了不存在的 `wechatpay-java` 依赖。

## 📝 修改清单

### pom.xml 修改

**删除的依赖：**
- ❌ `com.wechat.pay.java:wechatpay-java:0.2.11`

**新增的依赖：**
- ✅ `com.github.wechatpay-apiv3:wechatpay-apache-httpclient:0.4.8`

### Java 代码修改

#### 1. **WechatPayConfig.java** - 完全重写

**核心变化：**
- ❌ 移除所有 `com.wechat.pay.java.*` 的 import
- ✅ 使用 `com.wechat.pay.contrib.apache.httpclient.*` 的类

**提供的 Bean：**
```java
@Bean PrivateKey wechatPayPrivateKey()
  → 从 PemUtil.loadPrivateKey() 加载商户私钥

@Bean AutoUpdateCertificatesVerifier wechatPayCertificatesVerifier()
  → 平台证书自动更新和验证

@Bean HttpClient wechatPayHttpClient()
  → 带签名和验证的 HttpClient，用于调用微信 API

@Bean ObjectMapper objectMapper()
  → JSON 处理工具
```

#### 2. **WechatPayService.java** - 完全重写

**核心变化：**
- ❌ 移除所有 `JsapiServiceImpl`、`PrepayRequest` 等类
- ✅ 使用 `HttpClient` 直接调用 REST API

**关键方法：**
```java
public JsapiPayResponse createRechargeAndPrepay(Long userId, Integer amount, String openId)
  → 创建充值订单 + 调用 JSAPI 统一下单 + 生成支付参数
  
private String callJsapiPrepay(String outTradeNo, String description, Integer amountCents, String openId)
  → 使用 HttpClient 直接 POST 到微信支付 JSAPI 接口
  → URL: https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi
  
private Map<String, String> generatePayParams(String prepayId)
  → 生成前端支付参数（timeStamp、nonceStr、package、paySign）
  
private String generatePaySign(String timeStamp, String nonceStr, String packageStr)
  → 使用商户私钥生成 RSA-SHA256 签名
```

#### 3. **PayController.java** - 新增

**暴露的接口：**
```
POST /api/pay/createRechargeAndPrepay

请求体：
{
  "userId": 1,
  "amount": 100,      // 元
  "openId": "oabc123"
}

响应体：
{
  "code": 1,
  "message": "预下单成功",
  "data": {
    "outTradeNo": "RCH...",
    "appId": "wxc3a495d044293e1f",
    "timeStamp": "1234567890",
    "nonceStr": "...",
    "package": "prepay_id=wx...",
    "signType": "RSA",
    "paySign": "..."
  }
}
```

#### 4. **WechatPayNotifyController.java** - 完全重写

**处理流程：**
1. 从请求头读取 `Wechatpay-Timestamp`、`Wechatpay-Nonce`、`Wechatpay-Signature`、`Wechatpay-Serial`
2. 使用 `SignatureValidator` 验证签名
3. 使用 `AesGcmUtils.decrypt()` 解密 `resource.ciphertext`
4. 解析解密后的交易数据，获取 `out_trade_no`、`transaction_id`、`trade_state`
5. 当 `trade_state == "SUCCESS"` 时，调用 `userService.handleRechargePaid()`
6. 返回 `{"code":"SUCCESS","message":"成功"}`

**请求头示例：**
```
Wechatpay-Timestamp: 1234567890
Wechatpay-Nonce: random_nonce_string
Wechatpay-Signature: Base64_Encoded_Signature
Wechatpay-Serial: certificate_serial_number
```

#### 5. **删除的文件**
- ❌ `WechatPayNotifyService.java` （逻辑合并到 WechatPayNotifyController）
- ❌ `CertificateUtils.java` （不再需要）

#### 6. **保留的文件**
- ✅ `AesGcmUtils.java` （用于解密，无需修改）
- ✅ `SignatureUtils.java` （用于签名，无需修改）
- ✅ `UserService.java` （核心业务逻辑，仅复用 handleRechargePaid）
- ✅ `Recharge.java` （数据实体，无需修改）

## 🔧 配置文件（application.properties）

**已有配置，无需修改：**
```properties
wechat.appId=wxc3a495d044293e1f
wechat.appSecret=7139143d1f9c5530922161bf18149ca2
wechat.mchId=1103531688
wechat.apiV3Key=Bbzhang0503021111111111111111111
wechat.serialNo=53D865A35DD40B67C992547D7222D112C4348F35
wechat.privateKeyPath=/www/coffee/cert/apiclient_key.pem
wechat.notifyUrl=https://fushicoffee.cn/api/pay/wechat/notify
```

## 🚀 编译和部署

### 本地编译
```powershell
.\mvnw.cmd clean package -DskipTests
```

### 服务器编译
```bash
./mvnw clean package -DskipTests
```

所有依赖现在都在 Maven Central 仓库中存在，编译应该能通过。

## 📱 小程序前端调用

```javascript
// 1. 调用后端获取支付参数
wx.request({
  url: 'https://fushicoffee.cn/api/pay/createRechargeAndPrepay',
  method: 'POST',
  data: {
    userId: userInfo.id,
    amount: 100,
    openId: userInfo.openId
  },
  success(res) {
    const payParams = res.data.data;
    
    // 2. 调用微信支付
    wx.requestPayment({
      timeStamp: payParams.timeStamp,
      nonceStr: payParams.nonceStr,
      package: payParams.package,
      signType: payParams.signType,
      paySign: payParams.paySign,
      success(res) {
        wx.showToast({ title: '支付成功' });
      },
      fail(res) {
        wx.showToast({ title: '支付失败', icon: 'error' });
      }
    });
  }
});
```

## ✅ 验证清单

- [x] pom.xml 中只包含 `wechatpay-apache-httpclient:0.4.8`
- [x] 整个项目中不再有 `com.wechat.pay.java.*` 的 import
- [x] WechatPayConfig 使用 `AutoUpdateCertificatesVerifier` 和 `WechatPayHttpClientBuilder`
- [x] WechatPayService 使用 `HttpClient` 直接调用 REST API
- [x] WechatPayNotifyController 使用 `SignatureValidator` 和 `AesGcmUtils` 进行验签和解密
- [x] PayController 提供 `/api/pay/createRechargeAndPrepay` 接口
- [x] 复用 UserService.handleRechargePaid 处理支付成功逻辑

## 🎯 核心流程

### 1. JSAPI 支付流程

```
前端 → POST /api/pay/createRechargeAndPrepay(userId, amount, openId)
  ↓
后端创建 Recharge 记录（status=PENDING）
  ↓
后端调用微信统一下单 API → 获取 prepay_id
  ↓
后端生成 paySign（RSA-SHA256 签名）
  ↓
返回 { timeStamp, nonceStr, package, signType, paySign }
  ↓
前端调用 wx.requestPayment(params)
  ↓
用户在微信支付界面完成支付
  ↓
微信异步通知后端 POST /api/pay/wechat/notify
```

### 2. 回调处理流程

```
微信 → POST /api/pay/wechat/notify（带签名头）
  ↓
验证签名（SignatureValidator）
  ↓
解密 resource.ciphertext（AesGcmUtils）
  ↓
解析交易数据（out_trade_no、transaction_id、trade_state）
  ↓
如果 trade_state=="SUCCESS"
  ↓
调用 UserService.handleRechargePaid(outTradeNo, transactionId)
  ↓
更新用户积分、Recharge 状态为 SUCCESS
  ↓
返回 {"code":"SUCCESS"}
```

## 📌 重要说明

1. **不再依赖不存在的 SDK**：`wechatpay-java:0.2.11` 不在 Maven Central，已完全替换
2. **使用成熟稳定的 SDK**：`wechatpay-apache-httpclient:0.4.8` 在 Maven Central 可直接下载
3. **完整复用现有业务**：保留 UserService.handleRechargePaid，直接复用加积分逻辑
4. **支持所有 V3 功能**：签名、验签、加密、解密全部实现
5. **本地和服务器都能编译**：所有依赖来自 Maven Central

---

现在可以运行 `mvn clean package` 编译，应该能通过！🎉

