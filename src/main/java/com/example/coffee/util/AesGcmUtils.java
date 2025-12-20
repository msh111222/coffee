package com.example.coffee.util;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES-GCM 解密工具类
 * 用于解密微信支付 V3 回调中的 resource.ciphertext 字段
 */
public class AesGcmUtils {

    private static final int TAG_LENGTH_BIT = 128;
    private static final int NONCE_LENGTH_BYTE = 12;

    /**
     * 使用 AES-GCM 算法解密
     *
     * @param apiV3Key      微信支付 API v3 密钥
     * @param ciphertext    Base64 编码的密文
     * @param associatedData 附加认证数据
     * @param nonce         随机数
     * @return 解密后的明文
     */
    public static String decrypt(String apiV3Key, String ciphertext, String associatedData, String nonce) throws Exception {
        // 创建密钥
        byte[] keyBytes = apiV3Key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");

        // 初始化 Cipher
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce.getBytes(StandardCharsets.UTF_8));

        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        // 处理附加认证数据
        if (associatedData != null) {
            cipher.updateAAD(associatedData.getBytes(StandardCharsets.UTF_8));
        }

        // 解密
        byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertext);
        byte[] decrypted = cipher.doFinal(ciphertextBytes);

        return new String(decrypted, StandardCharsets.UTF_8);
    }
}

