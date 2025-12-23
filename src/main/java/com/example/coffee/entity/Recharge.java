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
   name = "recharge"
)
public class Recharge {
   @Id
   @GeneratedValue(
      strategy = GenerationType.IDENTITY
   )
   private Long id;
   @Column(
      nullable = false
   )
   private Long userId;
   @Column(
      nullable = false
   )
   private Integer amount;
   @Column(
      nullable = false
   )
   private String status = "success";
   @Temporal(TemporalType.TIMESTAMP)
   private Date createTime;
   @Column(
      columnDefinition = "VARCHAR(64)"
   )
   private String transactionId;
   @Column(
      columnDefinition = "VARCHAR(64)"
   )
   private String outTradeNo;
   @Temporal(TemporalType.TIMESTAMP)
   private Date payTime;
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
   public Long getUserId() {
      return this.userId;
   }

   @Generated
   public Integer getAmount() {
      return this.amount;
   }

   @Generated
   public String getStatus() {
      return this.status;
   }

   @Generated
   public Date getCreateTime() {
      return this.createTime;
   }

   @Generated
   public String getTransactionId() {
      return this.transactionId;
   }

   @Generated
   public String getOutTradeNo() {
      return this.outTradeNo;
   }

   @Generated
   public Date getPayTime() {
      return this.payTime;
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
   public void setAmount(final Integer amount) {
      this.amount = amount;
   }

   @Generated
   public void setStatus(final String status) {
      this.status = status;
   }

   @Generated
   public void setCreateTime(final Date createTime) {
      this.createTime = createTime;
   }

   @Generated
   public void setTransactionId(final String transactionId) {
      this.transactionId = transactionId;
   }

   @Generated
   public void setOutTradeNo(final String outTradeNo) {
      this.outTradeNo = outTradeNo;
   }

   @Generated
   public void setPayTime(final Date payTime) {
      this.payTime = payTime;
   }

   @Generated
   public void setUpdateTime(final Date updateTime) {
      this.updateTime = updateTime;
   }

   @Generated
   public boolean equals(final Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Recharge)) {
         return false;
      } else {
         Recharge other = (Recharge)o;
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

            Object this$userId = this.getUserId();
            Object other$userId = other.getUserId();
            if (this$userId == null) {
               if (other$userId != null) {
                  return false;
               }
            } else if (!this$userId.equals(other$userId)) {
               return false;
            }

            label105: {
               Object this$amount = this.getAmount();
               Object other$amount = other.getAmount();
               if (this$amount == null) {
                  if (other$amount == null) {
                     break label105;
                  }
               } else if (this$amount.equals(other$amount)) {
                  break label105;
               }

               return false;
            }

            Object this$status = this.getStatus();
            Object other$status = other.getStatus();
            if (this$status == null) {
               if (other$status != null) {
                  return false;
               }
            } else if (!this$status.equals(other$status)) {
               return false;
            }

            label91: {
               Object this$createTime = this.getCreateTime();
               Object other$createTime = other.getCreateTime();
               if (this$createTime == null) {
                  if (other$createTime == null) {
                     break label91;
                  }
               } else if (this$createTime.equals(other$createTime)) {
                  break label91;
               }

               return false;
            }

            Object this$transactionId = this.getTransactionId();
            Object other$transactionId = other.getTransactionId();
            if (this$transactionId == null) {
               if (other$transactionId != null) {
                  return false;
               }
            } else if (!this$transactionId.equals(other$transactionId)) {
               return false;
            }

            label77: {
               Object this$outTradeNo = this.getOutTradeNo();
               Object other$outTradeNo = other.getOutTradeNo();
               if (this$outTradeNo == null) {
                  if (other$outTradeNo == null) {
                     break label77;
                  }
               } else if (this$outTradeNo.equals(other$outTradeNo)) {
                  break label77;
               }

               return false;
            }

            label70: {
               Object this$payTime = this.getPayTime();
               Object other$payTime = other.getPayTime();
               if (this$payTime == null) {
                  if (other$payTime == null) {
                     break label70;
                  }
               } else if (this$payTime.equals(other$payTime)) {
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
      return other instanceof Recharge;
   }

   @Generated
   public int hashCode() {
      int PRIME = 31;
      int result = 1;
      Object $id = this.getId();
      result = result * 59 + ($id == null ? 43 : $id.hashCode());
      Object $userId = this.getUserId();
      result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
      Object $amount = this.getAmount();
      result = result * 59 + ($amount == null ? 43 : $amount.hashCode());
      Object $status = this.getStatus();
      result = result * 59 + ($status == null ? 43 : $status.hashCode());
      Object $createTime = this.getCreateTime();
      result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
      Object $transactionId = this.getTransactionId();
      result = result * 59 + ($transactionId == null ? 43 : $transactionId.hashCode());
      Object $outTradeNo = this.getOutTradeNo();
      result = result * 59 + ($outTradeNo == null ? 43 : $outTradeNo.hashCode());
      Object $payTime = this.getPayTime();
      result = result * 59 + ($payTime == null ? 43 : $payTime.hashCode());
      Object $updateTime = this.getUpdateTime();
      result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
      return result;
   }

   @Generated
   public String toString() {
      Long var10000 = this.getId();
      return "Recharge(id=" + var10000 + ", userId=" + this.getUserId() + ", amount=" + this.getAmount() + ", status=" + this.getStatus() + ", createTime=" + this.getCreateTime() + ", transactionId=" + this.getTransactionId() + ", outTradeNo=" + this.getOutTradeNo() + ", payTime=" + this.getPayTime() + ", updateTime=" + this.getUpdateTime() + ")";
   }
}
