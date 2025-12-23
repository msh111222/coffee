package com.example.coffee.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.codec.binary.Base64;

public class SignatureUtils {
   public static PrivateKey getPrivateKeyFromPem(String filePath) throws Exception {
      StringBuilder sb = new StringBuilder();
      BufferedReader br = new BufferedReader(new FileReader(filePath));

      String line;
      try {
         while((line = br.readLine()) != null) {
            if (!line.contains("BEGIN") && !line.contains("END")) {
               sb.append(line);
            }
         }
      } catch (Throwable var6) {
         try {
            br.close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      br.close();
      byte[] decodedKey = Base64.decodeBase64(sb.toString());
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
      KeyFactory var4 = KeyFactory.getInstance("RSA");
      return var4.generatePrivate(keySpec);
   }

   public static String sign(String message, PrivateKey privateKey) throws Exception {
      Signature signature = Signature.getInstance("SHA256WithRSA");
      signature.initSign(privateKey);
      signature.update(message.getBytes(StandardCharsets.UTF_8));
      byte[] signBytes = signature.sign();
      return Base64.encodeBase64String(signBytes);
   }

   public static boolean verify(String message, String sign, PublicKey publicKey) throws Exception {
      Signature signature = Signature.getInstance("SHA256WithRSA");
      signature.initVerify(publicKey);
      signature.update(message.getBytes(StandardCharsets.UTF_8));
      byte[] signBytes = Base64.decodeBase64(sign);
      return signature.verify(signBytes);
   }

   public static PublicKey getPublicKeyFromBase64(String publicKeyBase64) throws Exception {
      byte[] decodedKey = Base64.decodeBase64(publicKeyBase64);
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePublic(keySpec);
   }
}
