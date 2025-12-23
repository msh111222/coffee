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
   name = "admin"
)
public class Admin {
   @Id
   @GeneratedValue(
      strategy = GenerationType.IDENTITY
   )
   private Long id;
   @Column(
      unique = true,
      nullable = false
   )
   private String username;
   @Column(
      nullable = false
   )
   private String password;
   @Column(
      nullable = false
   )
   private String realName;
   @Column(
      nullable = false
   )
   private Integer role;
   @Column(
      nullable = false
   )
   private Boolean isActive = true;
   @Temporal(TemporalType.TIMESTAMP)
   private Date lastLoginTime;
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
   public String getUsername() {
      return this.username;
   }

   @Generated
   public String getPassword() {
      return this.password;
   }

   @Generated
   public String getRealName() {
      return this.realName;
   }

   @Generated
   public Integer getRole() {
      return this.role;
   }

   @Generated
   public Boolean getIsActive() {
      return this.isActive;
   }

   @Generated
   public Date getLastLoginTime() {
      return this.lastLoginTime;
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
   public void setUsername(final String username) {
      this.username = username;
   }

   @Generated
   public void setPassword(final String password) {
      this.password = password;
   }

   @Generated
   public void setRealName(final String realName) {
      this.realName = realName;
   }

   @Generated
   public void setRole(final Integer role) {
      this.role = role;
   }

   @Generated
   public void setIsActive(final Boolean isActive) {
      this.isActive = isActive;
   }

   @Generated
   public void setLastLoginTime(final Date lastLoginTime) {
      this.lastLoginTime = lastLoginTime;
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
      } else if (!(o instanceof Admin)) {
         return false;
      } else {
         Admin other = (Admin)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label119: {
               Object this$id = this.getId();
               Object other$id = other.getId();
               if (this$id == null) {
                  if (other$id == null) {
                     break label119;
                  }
               } else if (this$id.equals(other$id)) {
                  break label119;
               }

               return false;
            }

            Object this$role = this.getRole();
            Object other$role = other.getRole();
            if (this$role == null) {
               if (other$role != null) {
                  return false;
               }
            } else if (!this$role.equals(other$role)) {
               return false;
            }

            label105: {
               Object this$isActive = this.getIsActive();
               Object other$isActive = other.getIsActive();
               if (this$isActive == null) {
                  if (other$isActive == null) {
                     break label105;
                  }
               } else if (this$isActive.equals(other$isActive)) {
                  break label105;
               }

               return false;
            }

            Object this$username = this.getUsername();
            Object other$username = other.getUsername();
            if (this$username == null) {
               if (other$username != null) {
                  return false;
               }
            } else if (!this$username.equals(other$username)) {
               return false;
            }

            label91: {
               Object this$password = this.getPassword();
               Object other$password = other.getPassword();
               if (this$password == null) {
                  if (other$password == null) {
                     break label91;
                  }
               } else if (this$password.equals(other$password)) {
                  break label91;
               }

               return false;
            }

            Object this$realName = this.getRealName();
            Object other$realName = other.getRealName();
            if (this$realName == null) {
               if (other$realName != null) {
                  return false;
               }
            } else if (!this$realName.equals(other$realName)) {
               return false;
            }

            label77: {
               Object this$lastLoginTime = this.getLastLoginTime();
               Object other$lastLoginTime = other.getLastLoginTime();
               if (this$lastLoginTime == null) {
                  if (other$lastLoginTime == null) {
                     break label77;
                  }
               } else if (this$lastLoginTime.equals(other$lastLoginTime)) {
                  break label77;
               }

               return false;
            }

            label70: {
               Object this$createTime = this.getCreateTime();
               Object other$createTime = other.getCreateTime();
               if (this$createTime == null) {
                  if (other$createTime == null) {
                     break label70;
                  }
               } else if (this$createTime.equals(other$createTime)) {
                  break label70;
               }

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
      return other instanceof Admin;
   }

   @Generated
   public int hashCode() {
      int PRIME = 31;
      int result = 1;
      Object $id = this.getId();
      result = result * 59 + ($id == null ? 43 : $id.hashCode());
      Object $role = this.getRole();
      result = result * 59 + ($role == null ? 43 : $role.hashCode());
      Object $isActive = this.getIsActive();
      result = result * 59 + ($isActive == null ? 43 : $isActive.hashCode());
      Object $username = this.getUsername();
      result = result * 59 + ($username == null ? 43 : $username.hashCode());
      Object $password = this.getPassword();
      result = result * 59 + ($password == null ? 43 : $password.hashCode());
      Object $realName = this.getRealName();
      result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
      Object $lastLoginTime = this.getLastLoginTime();
      result = result * 59 + ($lastLoginTime == null ? 43 : $lastLoginTime.hashCode());
      Object $createTime = this.getCreateTime();
      result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
      Object $updateTime = this.getUpdateTime();
      result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
      return result;
   }

   @Generated
   public String toString() {
      Long var10000 = this.getId();
      return "Admin(id=" + var10000 + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", realName=" + this.getRealName() + ", role=" + this.getRole() + ", isActive=" + this.getIsActive() + ", lastLoginTime=" + this.getLastLoginTime() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
   }
}
