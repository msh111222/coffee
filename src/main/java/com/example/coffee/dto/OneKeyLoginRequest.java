package com.example.coffee.dto;

import lombok.Generated;

public class OneKeyLoginRequest {
   private String code;
   private String openId;
   private String nickName;
   private String avatarUrl;
   private String encryptedData;
   private String iv;

   @Generated
   public String getCode() {
      return this.code;
   }

   @Generated
   public String getOpenId() {
      return this.openId;
   }

   @Generated
   public String getNickName() {
      return this.nickName;
   }

   @Generated
   public String getAvatarUrl() {
      return this.avatarUrl;
   }

   @Generated
   public String getEncryptedData() {
      return this.encryptedData;
   }

   @Generated
   public String getIv() {
      return this.iv;
   }

   @Generated
   public void setCode(final String code) {
      this.code = code;
   }

   @Generated
   public void setOpenId(final String openId) {
      this.openId = openId;
   }

   @Generated
   public void setNickName(final String nickName) {
      this.nickName = nickName;
   }

   @Generated
   public void setAvatarUrl(final String avatarUrl) {
      this.avatarUrl = avatarUrl;
   }

   @Generated
   public void setEncryptedData(final String encryptedData) {
      this.encryptedData = encryptedData;
   }

   @Generated
   public void setIv(final String iv) {
      this.iv = iv;
   }

   @Generated
   public boolean equals(final Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof OneKeyLoginRequest)) {
         return false;
      } else {
         OneKeyLoginRequest other = (OneKeyLoginRequest)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$code = this.getCode();
            Object other$code = other.getCode();
            if (this$code == null) {
               if (other$code != null) {
                  return false;
               }
            } else if (!this$code.equals(other$code)) {
               return false;
            }

            Object this$openId = this.getOpenId();
            Object other$openId = other.getOpenId();
            if (this$openId == null) {
               if (other$openId != null) {
                  return false;
               }
            } else if (!this$openId.equals(other$openId)) {
               return false;
            }

            Object this$nickName = this.getNickName();
            Object other$nickName = other.getNickName();
            if (this$nickName == null) {
               if (other$nickName != null) {
                  return false;
               }
            } else if (!this$nickName.equals(other$nickName)) {
               return false;
            }

            label62: {
               Object this$avatarUrl = this.getAvatarUrl();
               Object other$avatarUrl = other.getAvatarUrl();
               if (this$avatarUrl == null) {
                  if (other$avatarUrl == null) {
                     break label62;
                  }
               } else if (this$avatarUrl.equals(other$avatarUrl)) {
                  break label62;
               }

               return false;
            }

            label55: {
               Object this$encryptedData = this.getEncryptedData();
               Object other$encryptedData = other.getEncryptedData();
               if (this$encryptedData == null) {
                  if (other$encryptedData == null) {
                     break label55;
                  }
               } else if (this$encryptedData.equals(other$encryptedData)) {
                  break label55;
               }

               return false;
            }

            Object this$iv = this.getIv();
            Object other$iv = other.getIv();
            if (this$iv == null) {
               if (other$iv != null) {
                  return false;
               }
            } else if (!this$iv.equals(other$iv)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return other instanceof OneKeyLoginRequest;
   }

   @Generated
   public int hashCode() {
      int PRIME = 31;
      int result = 1;
      Object $code = this.getCode();
      result = result * 59 + ($code == null ? 43 : $code.hashCode());
      Object $openId = this.getOpenId();
      result = result * 59 + ($openId == null ? 43 : $openId.hashCode());
      Object $nickName = this.getNickName();
      result = result * 59 + ($nickName == null ? 43 : $nickName.hashCode());
      Object $avatarUrl = this.getAvatarUrl();
      result = result * 59 + ($avatarUrl == null ? 43 : $avatarUrl.hashCode());
      Object $encryptedData = this.getEncryptedData();
      result = result * 59 + ($encryptedData == null ? 43 : $encryptedData.hashCode());
      Object $iv = this.getIv();
      result = result * 59 + ($iv == null ? 43 : $iv.hashCode());
      return result;
   }

   @Generated
   public String toString() {
      String var10000 = this.getCode();
      return "OneKeyLoginRequest(code=" + var10000 + ", openId=" + this.getOpenId() + ", nickName=" + this.getNickName() + ", avatarUrl=" + this.getAvatarUrl() + ", encryptedData=" + this.getEncryptedData() + ", iv=" + this.getIv() + ")";
   }
}
