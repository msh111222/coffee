package com.example.coffee.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Generated;

@Entity
@Table(name = "user")
public class User {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(unique = true, nullable = false)
   private String openId;

   private String nickName;
   private String avatarUrl;
   private String phoneNumber;
   private String personalInfo;
   private Integer points = 0;
   private Long consumption = 0L;

   // 新增：生日（仅日期）
   @Temporal(TemporalType.DATE)
   @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
   private Date birthday;

   // 新增：收货地址
   @Column(length = 512)
   private String shippingAddress;

   @Temporal(TemporalType.TIMESTAMP)
   private Date createTime;

   @Temporal(TemporalType.TIMESTAMP)
   private Date updateTime;

   @PrePersist
   protected void onCreate() {
      this.createTime = new Date();
      this.updateTime = new Date();
   }

   @PreUpdate
   protected void onUpdate() {
      this.updateTime = new Date();
   }

   @Generated
   public Long getId() {
      return this.id;
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
   public String getPhoneNumber() {
      return this.phoneNumber;
   }

   @Generated
   public String getPersonalInfo() {
      return this.personalInfo;
   }

   @Generated
   public Integer getPoints() {
      return this.points;
   }

   @Generated
   public Long getConsumption() {
      return this.consumption;
   }

   public Date getBirthday() {
      return this.birthday;
   }

   public String getShippingAddress() {
      return this.shippingAddress;
   }

   @Generated
   public Date getCreateTime() {
      return this.createTime;
   }

   @Generated
   public Date getUpdateTime() {
      return this.updateTime;
   }

   @Generated
   public void setId(final Long id) {
      this.id = id;
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
   public void setPhoneNumber(final String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   @Generated
   public void setPersonalInfo(final String personalInfo) {
      this.personalInfo = personalInfo;
   }

   @Generated
   public void setPoints(final Integer points) {
      this.points = points;
   }

   @Generated
   public void setConsumption(final Long consumption) {
      this.consumption = consumption;
   }

   public void setBirthday(final Date birthday) {
      this.birthday = birthday;
   }

   public void setShippingAddress(final String shippingAddress) {
      this.shippingAddress = shippingAddress;
   }

   @Generated
   public void setCreateTime(final Date createTime) {
      this.createTime = createTime;
   }

   @Generated
   public void setUpdateTime(final Date updateTime) {
      this.updateTime = updateTime;
   }

   @Generated
   public boolean equals(final Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof User)) {
         return false;
      } else {
         User other = (User)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$id = this.getId();
            Object other$id = other.getId();
            if (this$id == null) {
               if (other$id != null) {
                  return false;
               }
            } else if (!this$id.equals(other$id)) {
               return false;
            }

            Object this$points = this.getPoints();
            Object other$points = other.getPoints();
            if (this$points == null) {
               if (other$points != null) {
                  return false;
               }
            } else if (!this$points.equals(other$points)) {
               return false;
            }

            Object this$consumption = this.getConsumption();
            Object other$consumption = other.getConsumption();
            if (this$consumption == null) {
               if (other$consumption != null) {
                  return false;
               }
            } else if (!this$consumption.equals(other$consumption)) {
               return false;
            }

            label140: {
               Object this$openId = this.getOpenId();
               Object other$openId = other.getOpenId();
               if (this$openId == null) {
                  if (other$openId == null) {
                     break label140;
                  }
               } else if (this$openId.equals(other$openId)) {
                  break label140;
               }

               return false;
            }

            label133: {
               Object this$nickName = this.getNickName();
               Object other$nickName = other.getNickName();
               if (this$nickName == null) {
                  if (other$nickName == null) {
                     break label133;
                  }
               } else if (this$nickName.equals(other$nickName)) {
                  break label133;
               }

               return false;
            }

            Object this$avatarUrl = this.getAvatarUrl();
            Object other$avatarUrl = other.getAvatarUrl();
            if (this$avatarUrl == null) {
               if (other$avatarUrl != null) {
                  return false;
               }
            } else if (!this$avatarUrl.equals(other$avatarUrl)) {
               return false;
            }

            label119: {
               Object this$phoneNumber = this.getPhoneNumber();
               Object other$phoneNumber = other.getPhoneNumber();
               if (this$phoneNumber == null) {
                  if (other$phoneNumber == null) {
                     break label119;
                  }
               } else if (this$phoneNumber.equals(other$phoneNumber)) {
                  break label119;
               }

               return false;
            }

            label112: {
               Object this$personalInfo = this.getPersonalInfo();
               Object other$personalInfo = other.getPersonalInfo();
               if (this$personalInfo == null) {
                  if (other$personalInfo == null) {
                     break label112;
                  }
               } else if (this$personalInfo.equals(other$personalInfo)) {
                  break label112;
               }

               return false;
            }

            label105: {
               Object this$birthday = this.getBirthday();
               Object other$birthday = other.getBirthday();
               if (this$birthday == null) {
                  if (other$birthday == null) {
                     break label105;
                  }
               } else if (this$birthday.equals(other$birthday)) {
                  break label105;
               }

               return false;
            }

            label98: {
               Object this$shippingAddress = this.getShippingAddress();
               Object other$shippingAddress = other.getShippingAddress();
               if (this$shippingAddress == null) {
                  if (other$shippingAddress == null) {
                     break label98;
                  }
               } else if (this$shippingAddress.equals(other$shippingAddress)) {
                  break label98;
               }

               return false;
            }

            Object this$createTime = this.getCreateTime();
            Object other$createTime = other.getCreateTime();
            if (this$createTime == null) {
               if (other$createTime != null) {
                  return false;
               }
            } else if (!this$createTime.equals(other$createTime)) {
               return false;
            }

            Object this$updateTime = this.getUpdateTime();
            Object other$updateTime = other.getUpdateTime();
            if (this$updateTime == null) {
               if (other$updateTime != null) {
                  return false;
               }
            } else if (!this$updateTime.equals(other$updateTime)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return other instanceof User;
   }

   @Generated
   public int hashCode() {
      int PRIME = 31;
      int result = 1;
      Object $id = this.getId();
      result = result * 59 + ($id == null ? 43 : $id.hashCode());
      Object $points = this.getPoints();
      result = result * 59 + ($points == null ? 43 : $points.hashCode());
      Object $consumption = this.getConsumption();
      result = result * 59 + ($consumption == null ? 43 : $consumption.hashCode());
      Object $openId = this.getOpenId();
      result = result * 59 + ($openId == null ? 43 : $openId.hashCode());
      Object $nickName = this.getNickName();
      result = result * 59 + ($nickName == null ? 43 : $nickName.hashCode());
      Object $avatarUrl = this.getAvatarUrl();
      result = result * 59 + ($avatarUrl == null ? 43 : $avatarUrl.hashCode());
      Object $phoneNumber = this.getPhoneNumber();
      result = result * 59 + ($phoneNumber == null ? 43 : $phoneNumber.hashCode());
      Object $personalInfo = this.getPersonalInfo();
      result = result * 59 + ($personalInfo == null ? 43 : $personalInfo.hashCode());
      Object $birthday = this.getBirthday();
      result = result * 59 + ($birthday == null ? 43 : $birthday.hashCode());
      Object $shippingAddress = this.getShippingAddress();
      result = result * 59 + ($shippingAddress == null ? 43 : $shippingAddress.hashCode());
      Object $createTime = this.getCreateTime();
      result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
      Object $updateTime = this.getUpdateTime();
      result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
      return result;
   }

   @Generated
   public String toString() {
      Long var10000 = this.getId();
      return "User(id=" + var10000
         + ", openId=" + this.getOpenId()
         + ", nickName=" + this.getNickName()
         + ", avatarUrl=" + this.getAvatarUrl()
         + ", phoneNumber=" + this.getPhoneNumber()
         + ", personalInfo=" + this.getPersonalInfo()
         + ", points=" + this.getPoints()
         + ", consumption=" + this.getConsumption()
         + ", birthday=" + this.getBirthday()
         + ", shippingAddress=" + this.getShippingAddress()
         + ", createTime=" + this.getCreateTime()
         + ", updateTime=" + this.getUpdateTime()
         + ")";
   }
}