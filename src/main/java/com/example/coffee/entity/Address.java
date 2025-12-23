package com.example.coffee.entity;

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
@Table(
   name = "address"
)
public class Address {
   @Id
   @GeneratedValue(
      strategy = GenerationType.IDENTITY
   )
   private Long id;
   @Column(
      nullable = false
   )
   private Long userId;
   private String recipientName;
   private String phoneNumber;
   private String province;
   private String city;
   private String district;
   private String detail;
   @Column(
      nullable = false,
      columnDefinition = "TINYINT(1) DEFAULT 0"
   )
   private Boolean isDefault = false;
   @Temporal(TemporalType.TIMESTAMP)
   private Date createTime;
   @Temporal(TemporalType.TIMESTAMP)
   private Date updateTime;

   @PrePersist
   protected void onCreate() {
      if (this.isDefault == null) {
         this.isDefault = false;
      }

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
   public Long getUserId() {
      return this.userId;
   }

   @Generated
   public String getRecipientName() {
      return this.recipientName;
   }

   @Generated
   public String getPhoneNumber() {
      return this.phoneNumber;
   }

   @Generated
   public String getProvince() {
      return this.province;
   }

   @Generated
   public String getCity() {
      return this.city;
   }

   @Generated
   public String getDistrict() {
      return this.district;
   }

   @Generated
   public String getDetail() {
      return this.detail;
   }

   @Generated
   public Boolean getIsDefault() {
      return this.isDefault;
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
   public void setUserId(final Long userId) {
      this.userId = userId;
   }

   @Generated
   public void setRecipientName(final String recipientName) {
      this.recipientName = recipientName;
   }

   @Generated
   public void setPhoneNumber(final String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   @Generated
   public void setProvince(final String province) {
      this.province = province;
   }

   @Generated
   public void setCity(final String city) {
      this.city = city;
   }

   @Generated
   public void setDistrict(final String district) {
      this.district = district;
   }

   @Generated
   public void setDetail(final String detail) {
      this.detail = detail;
   }

   @Generated
   public void setIsDefault(final Boolean isDefault) {
      this.isDefault = isDefault;
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
      } else if (!(o instanceof Address)) {
         return false;
      } else {
         Address other = (Address)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label143: {
               Object this$id = this.getId();
               Object other$id = other.getId();
               if (this$id == null) {
                  if (other$id == null) {
                     break label143;
                  }
               } else if (this$id.equals(other$id)) {
                  break label143;
               }

               return false;
            }

            Object this$userId = this.getUserId();
            Object other$userId = other.getUserId();
            if (this$userId == null) {
               if (other$userId != null) {
                  return false;
               }
            } else if (!this$userId.equals(other$userId)) {
               return false;
            }

            Object this$isDefault = this.getIsDefault();
            Object other$isDefault = other.getIsDefault();
            if (this$isDefault == null) {
               if (other$isDefault != null) {
                  return false;
               }
            } else if (!this$isDefault.equals(other$isDefault)) {
               return false;
            }

            label122: {
               Object this$recipientName = this.getRecipientName();
               Object other$recipientName = other.getRecipientName();
               if (this$recipientName == null) {
                  if (other$recipientName == null) {
                     break label122;
                  }
               } else if (this$recipientName.equals(other$recipientName)) {
                  break label122;
               }

               return false;
            }

            label115: {
               Object this$phoneNumber = this.getPhoneNumber();
               Object other$phoneNumber = other.getPhoneNumber();
               if (this$phoneNumber == null) {
                  if (other$phoneNumber == null) {
                     break label115;
                  }
               } else if (this$phoneNumber.equals(other$phoneNumber)) {
                  break label115;
               }

               return false;
            }

            Object this$province = this.getProvince();
            Object other$province = other.getProvince();
            if (this$province == null) {
               if (other$province != null) {
                  return false;
               }
            } else if (!this$province.equals(other$province)) {
               return false;
            }

            Object this$city = this.getCity();
            Object other$city = other.getCity();
            if (this$city == null) {
               if (other$city != null) {
                  return false;
               }
            } else if (!this$city.equals(other$city)) {
               return false;
            }

            label94: {
               Object this$district = this.getDistrict();
               Object other$district = other.getDistrict();
               if (this$district == null) {
                  if (other$district == null) {
                     break label94;
                  }
               } else if (this$district.equals(other$district)) {
                  break label94;
               }

               return false;
            }

            label87: {
               Object this$detail = this.getDetail();
               Object other$detail = other.getDetail();
               if (this$detail == null) {
                  if (other$detail == null) {
                     break label87;
                  }
               } else if (this$detail.equals(other$detail)) {
                  break label87;
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
      return other instanceof Address;
   }

   @Generated
   public int hashCode() {
      int PRIME = 31;
      int result = 1;
      Object $id = this.getId();
      result = result * 59 + ($id == null ? 43 : $id.hashCode());
      Object $userId = this.getUserId();
      result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
      Object $isDefault = this.getIsDefault();
      result = result * 59 + ($isDefault == null ? 43 : $isDefault.hashCode());
      Object $recipientName = this.getRecipientName();
      result = result * 59 + ($recipientName == null ? 43 : $recipientName.hashCode());
      Object $phoneNumber = this.getPhoneNumber();
      result = result * 59 + ($phoneNumber == null ? 43 : $phoneNumber.hashCode());
      Object $province = this.getProvince();
      result = result * 59 + ($province == null ? 43 : $province.hashCode());
      Object $city = this.getCity();
      result = result * 59 + ($city == null ? 43 : $city.hashCode());
      Object $district = this.getDistrict();
      result = result * 59 + ($district == null ? 43 : $district.hashCode());
      Object $detail = this.getDetail();
      result = result * 59 + ($detail == null ? 43 : $detail.hashCode());
      Object $createTime = this.getCreateTime();
      result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
      Object $updateTime = this.getUpdateTime();
      result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
      return result;
   }

   @Generated
   public String toString() {
      Long var10000 = this.getId();
      return "Address(id=" + var10000 + ", userId=" + this.getUserId() + ", recipientName=" + this.getRecipientName() + ", phoneNumber=" + this.getPhoneNumber() + ", province=" + this.getProvince() + ", city=" + this.getCity() + ", district=" + this.getDistrict() + ", detail=" + this.getDetail() + ", isDefault=" + this.getIsDefault() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
   }
}
