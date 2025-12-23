package com.example.coffee.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesGcmUtils {
   private static final int TAG_LENGTH_BIT = 128;
   private static final int NONCE_LENGTH_BYTE = 12;

   public static String decrypt(String apiV3Key, String ciphertext, String associatedData, String nonce) throws Exception {
      byte[] keyBytes = apiV3Key.getBytes(StandardCharsets.UTF_8);
      SecretKeySpec key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      GCMParameterSpec spec = new GCMParameterSpec(128, nonce.getBytes(StandardCharsets.UTF_8));
      cipher.init(2, key, spec);
      if (associatedData != null) {
         cipher.updateAAD(associatedData.getBytes(StandardCharsets.UTF_8));
      }

      byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertext);
      byte[] decrypted = cipher.doFinal(ciphertextBytes);
      return new String(decrypted, StandardCharsets.UTF_8);
   }
}
