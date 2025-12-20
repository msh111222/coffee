package com.example.coffee.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 密钥和签名工具类
 */
public class SignatureUtils {

    /**
     * 从 PEM 格式的私钥文件读取私钥
     */
    public static PrivateKey getPrivateKeyFromPem(String filePath) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 跳过 PEM 格式的头尾标签
                if (!line.contains("BEGIN") && !line.contains("END")) {
                    sb.append(line);
                }
            }
        }
        
        byte[] decodedKey = Base64.decodeBase64(sb.toString());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 使用 RSA 私钥对字符串签名
     * 对应微信支付中的 paySign 生成逻辑
     */
    public static String sign(String message, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] signBytes = signature.sign();
        return Base64.encodeBase64String(signBytes);
    }

    /**
     * 使用 RSA 公钥验证签名
     * 用于验证微信回调的签名
     */
    public static boolean verify(String message, String sign, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initVerify(publicKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] signBytes = Base64.decodeBase64(sign);
        return signature.verify(signBytes);
    }

    /**
     * 从 Base64 编码的 X.509 格式公钥创建 PublicKey 对象
     */
    public static PublicKey getPublicKeyFromBase64(String publicKeyBase64) throws Exception {
        byte[] decodedKey = Base64.decodeBase64(publicKeyBase64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}

