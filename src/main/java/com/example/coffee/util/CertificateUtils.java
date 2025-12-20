package com.example.coffee.util;

import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.io.ByteArrayInputStream;
import java.security.PublicKey;
import java.util.Base64;

/**
 * 微信平台证书处理工具类
 * 用于处理微信支付平台证书相关的操作
 */
public class CertificateUtils {

    /**
     * 从 PEM 格式的证书字符串提取公钥
     * 
     * @param certificatePem PEM 格式的证书
     * @return 公钥
     */
    public static PublicKey getPublicKeyFromCertificatePem(String certificatePem) throws Exception {
        // 移除 PEM 格式的头尾标签
        String certContent = certificatePem
                .replace("-----BEGIN CERTIFICATE-----", "")
                .replace("-----END CERTIFICATE-----", "")
                .replace("\n", "")
                .replace("\r", "");

        // 解码 Base64
        byte[] decodedCert = Base64.getDecoder().decode(certContent);

        // 创建证书对象
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) cf.generateCertificate(
                new ByteArrayInputStream(decodedCert)
        );

        // 从证书中提取公钥
        return certificate.getPublicKey();
    }
}

