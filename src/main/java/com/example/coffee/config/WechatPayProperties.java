package com.example.coffee.config;

import lombok.Generated;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(
   prefix = "wechat"
)
public class WechatPayProperties {
   private String appId;
   private String appSecret;
   private String mchId;
   private String apiV3Key;
   private String serialNo;
   private String privateKeyPath;
   private String notifyUrl;

   @Generated
   public String getAppId() {
      return this.appId;
   }

   @Generated
   public String getAppSecret() {
      return this.appSecret;
   }

   @Generated
   public String getMchId() {
      return this.mchId;
   }

   @Generated
   public String getApiV3Key() {
      return this.apiV3Key;
   }

   @Generated
   public String getSerialNo() {
      return this.serialNo;
   }

   @Generated
   public String getPrivateKeyPath() {
      return this.privateKeyPath;
   }

   @Generated
   public String getNotifyUrl() {
      return this.notifyUrl;
   }

   @Generated
   public void setAppId(final String appId) {
      this.appId = appId;
   }

   @Generated
   public void setAppSecret(final String appSecret) {
      this.appSecret = appSecret;
   }

   @Generated
   public void setMchId(final String mchId) {
      this.mchId = mchId;
   }

   @Generated
   public void setApiV3Key(final String apiV3Key) {
      this.apiV3Key = apiV3Key;
   }

   @Generated
   public void setSerialNo(final String serialNo) {
      this.serialNo = serialNo;
   }

   @Generated
   public void setPrivateKeyPath(final String privateKeyPath) {
      this.privateKeyPath = privateKeyPath;
   }

   @Generated
   public void setNotifyUrl(final String notifyUrl) {
      this.notifyUrl = notifyUrl;
   }

   @Generated
   public boolean equals(final Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof WechatPayProperties)) {
         return false;
      } else {
         WechatPayProperties other = (WechatPayProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label95: {
               Object this$appId = this.getAppId();
               Object other$appId = other.getAppId();
               if (this$appId == null) {
                  if (other$appId == null) {
                     break label95;
                  }
               } else if (this$appId.equals(other$appId)) {
                  break label95;
               }

               return false;
            }

            Object this$appSecret = this.getAppSecret();
            Object other$appSecret = other.getAppSecret();
            if (this$appSecret == null) {
               if (other$appSecret != null) {
                  return false;
               }
            } else if (!this$appSecret.equals(other$appSecret)) {
               return false;
            }

            Object this$mchId = this.getMchId();
            Object other$mchId = other.getMchId();
            if (this$mchId == null) {
               if (other$mchId != null) {
                  return false;
               }
            } else if (!this$mchId.equals(other$mchId)) {
               return false;
            }

            label74: {
               Object this$apiV3Key = this.getApiV3Key();
               Object other$apiV3Key = other.getApiV3Key();
               if (this$apiV3Key == null) {
                  if (other$apiV3Key == null) {
                     break label74;
                  }
               } else if (this$apiV3Key.equals(other$apiV3Key)) {
                  break label74;
               }

               return false;
            }

            label67: {
               Object this$serialNo = this.getSerialNo();
               Object other$serialNo = other.getSerialNo();
               if (this$serialNo == null) {
                  if (other$serialNo == null) {
                     break label67;
                  }
               } else if (this$serialNo.equals(other$serialNo)) {
                  break label67;
               }

               return false;
            }

            Object this$privateKeyPath = this.getPrivateKeyPath();
            Object other$privateKeyPath = other.getPrivateKeyPath();
            if (this$privateKeyPath == null) {
               if (other$privateKeyPath != null) {
                  return false;
               }
            } else if (!this$privateKeyPath.equals(other$privateKeyPath)) {
               return false;
            }

            Object this$notifyUrl = this.getNotifyUrl();
            Object other$notifyUrl = other.getNotifyUrl();
            if (this$notifyUrl == null) {
               if (other$notifyUrl != null) {
                  return false;
               }
            } else if (!this$notifyUrl.equals(other$notifyUrl)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return other instanceof WechatPayProperties;
   }

   @Generated
   public int hashCode() {
      int PRIME = 31;
      int result = 1;
      Object $appId = this.getAppId();
      result = result * 59 + ($appId == null ? 43 : $appId.hashCode());
      Object $appSecret = this.getAppSecret();
      result = result * 59 + ($appSecret == null ? 43 : $appSecret.hashCode());
      Object $mchId = this.getMchId();
      result = result * 59 + ($mchId == null ? 43 : $mchId.hashCode());
      Object $apiV3Key = this.getApiV3Key();
      result = result * 59 + ($apiV3Key == null ? 43 : $apiV3Key.hashCode());
      Object $serialNo = this.getSerialNo();
      result = result * 59 + ($serialNo == null ? 43 : $serialNo.hashCode());
      Object $privateKeyPath = this.getPrivateKeyPath();
      result = result * 59 + ($privateKeyPath == null ? 43 : $privateKeyPath.hashCode());
      Object $notifyUrl = this.getNotifyUrl();
      result = result * 59 + ($notifyUrl == null ? 43 : $notifyUrl.hashCode());
      return result;
   }

   @Generated
   public String toString() {
      String var10000 = this.getAppId();
      return "WechatPayProperties(appId=" + var10000 + ", appSecret=" + this.getAppSecret() + ", mchId=" + this.getMchId() + ", apiV3Key=" + this.getApiV3Key() + ", serialNo=" + this.getSerialNo() + ", privateKeyPath=" + this.getPrivateKeyPath() + ", notifyUrl=" + this.getNotifyUrl() + ")";
   }
}
