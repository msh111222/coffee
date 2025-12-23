package com.example.coffee.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import lombok.Generated;

@Entity
@Table(
   name = "orders"
)
public class Order {
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
      unique = true,
      nullable = false
   )
   private String orderNo;
   @Column(
      nullable = false
   )
   private Double totalAmount;
   @Column(
      columnDefinition = "VARCHAR(20) DEFAULT '待支付'"
   )
   private String status = "待支付";
   @Column(
      columnDefinition = "VARCHAR(20) DEFAULT '待发货'"
   )
   private String shipStatus = "待发货";
   @Column(
      columnDefinition = "VARCHAR(20) DEFAULT '无'"
   )
   private String refundStatus = "无";
   @Column(
      nullable = false
   )
   private Long addressId;
   @Column(
      columnDefinition = "TEXT"
   )
   private String remark;
   @Column
   private String trackingNumber;
   @Column(
      columnDefinition = "VARCHAR(50)"
   )
   private String expressCompany;
   @Temporal(TemporalType.TIMESTAMP)
   private Date shipTime;
   @Temporal(TemporalType.TIMESTAMP)
   private Date refundTime;
   @Temporal(TemporalType.TIMESTAMP)
   private Date receiveTime;
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
   private Date createTime;
   @Temporal(TemporalType.TIMESTAMP)
   private Date updateTime;
   @OneToMany(
      mappedBy = "order",
      cascade = {CascadeType.ALL},
      fetch = FetchType.LAZY
   )
   private List<OrderItem> items;

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
   public String getOrderNo() {
      return this.orderNo;
   }

   @Generated
   public Double getTotalAmount() {
      return this.totalAmount;
   }

   @Generated
   public String getStatus() {
      return this.status;
   }

   @Generated
   public String getShipStatus() {
      return this.shipStatus;
   }

   @Generated
   public String getRefundStatus() {
      return this.refundStatus;
   }

   @Generated
   public Long getAddressId() {
      return this.addressId;
   }

   @Generated
   public String getRemark() {
      return this.remark;
   }

   @Generated
   public String getTrackingNumber() {
      return this.trackingNumber;
   }

   @Generated
   public String getExpressCompany() {
      return this.expressCompany;
   }

   @Generated
   public Date getShipTime() {
      return this.shipTime;
   }

   @Generated
   public Date getRefundTime() {
      return this.refundTime;
   }

   @Generated
   public Date getReceiveTime() {
      return this.receiveTime;
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
   public Date getCreateTime() {
      return this.createTime;
   }

   @Generated
   public Date getUpdateTime() {
      return this.updateTime;
   }

   @Generated
   public List<OrderItem> getItems() {
      return this.items;
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
   public void setOrderNo(final String orderNo) {
      this.orderNo = orderNo;
   }

   @Generated
   public void setTotalAmount(final Double totalAmount) {
      this.totalAmount = totalAmount;
   }

   @Generated
   public void setStatus(final String status) {
      this.status = status;
   }

   @Generated
   public void setShipStatus(final String shipStatus) {
      this.shipStatus = shipStatus;
   }

   @Generated
   public void setRefundStatus(final String refundStatus) {
      this.refundStatus = refundStatus;
   }

   @Generated
   public void setAddressId(final Long addressId) {
      this.addressId = addressId;
   }

   @Generated
   public void setRemark(final String remark) {
      this.remark = remark;
   }

   @Generated
   public void setTrackingNumber(final String trackingNumber) {
      this.trackingNumber = trackingNumber;
   }

   @Generated
   public void setExpressCompany(final String expressCompany) {
      this.expressCompany = expressCompany;
   }

   @Generated
   public void setShipTime(final Date shipTime) {
      this.shipTime = shipTime;
   }

   @Generated
   public void setRefundTime(final Date refundTime) {
      this.refundTime = refundTime;
   }

   @Generated
   public void setReceiveTime(final Date receiveTime) {
      this.receiveTime = receiveTime;
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
   public void setCreateTime(final Date createTime) {
      this.createTime = createTime;
   }

   @Generated
   public void setUpdateTime(final Date updateTime) {
      this.updateTime = updateTime;
   }

   @Generated
   public void setItems(final List<OrderItem> items) {
      this.items = items;
   }

   @Generated
   public boolean equals(final Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Order)) {
         return false;
      } else {
         Order other = (Order)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label251: {
               Object this$id = this.getId();
               Object other$id = other.getId();
               if (this$id == null) {
                  if (other$id == null) {
                     break label251;
                  }
               } else if (this$id.equals(other$id)) {
                  break label251;
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

            Object this$totalAmount = this.getTotalAmount();
            Object other$totalAmount = other.getTotalAmount();
            if (this$totalAmount == null) {
               if (other$totalAmount != null) {
                  return false;
               }
            } else if (!this$totalAmount.equals(other$totalAmount)) {
               return false;
            }

            label230: {
               Object this$addressId = this.getAddressId();
               Object other$addressId = other.getAddressId();
               if (this$addressId == null) {
                  if (other$addressId == null) {
                     break label230;
                  }
               } else if (this$addressId.equals(other$addressId)) {
                  break label230;
               }

               return false;
            }

            label223: {
               Object this$orderNo = this.getOrderNo();
               Object other$orderNo = other.getOrderNo();
               if (this$orderNo == null) {
                  if (other$orderNo == null) {
                     break label223;
                  }
               } else if (this$orderNo.equals(other$orderNo)) {
                  break label223;
               }

               return false;
            }

            label216: {
               Object this$status = this.getStatus();
               Object other$status = other.getStatus();
               if (this$status == null) {
                  if (other$status == null) {
                     break label216;
                  }
               } else if (this$status.equals(other$status)) {
                  break label216;
               }

               return false;
            }

            Object this$shipStatus = this.getShipStatus();
            Object other$shipStatus = other.getShipStatus();
            if (this$shipStatus == null) {
               if (other$shipStatus != null) {
                  return false;
               }
            } else if (!this$shipStatus.equals(other$shipStatus)) {
               return false;
            }

            label202: {
               Object this$refundStatus = this.getRefundStatus();
               Object other$refundStatus = other.getRefundStatus();
               if (this$refundStatus == null) {
                  if (other$refundStatus == null) {
                     break label202;
                  }
               } else if (this$refundStatus.equals(other$refundStatus)) {
                  break label202;
               }

               return false;
            }

            Object this$remark = this.getRemark();
            Object other$remark = other.getRemark();
            if (this$remark == null) {
               if (other$remark != null) {
                  return false;
               }
            } else if (!this$remark.equals(other$remark)) {
               return false;
            }

            label188: {
               Object this$trackingNumber = this.getTrackingNumber();
               Object other$trackingNumber = other.getTrackingNumber();
               if (this$trackingNumber == null) {
                  if (other$trackingNumber == null) {
                     break label188;
                  }
               } else if (this$trackingNumber.equals(other$trackingNumber)) {
                  break label188;
               }

               return false;
            }

            Object this$expressCompany = this.getExpressCompany();
            Object other$expressCompany = other.getExpressCompany();
            if (this$expressCompany == null) {
               if (other$expressCompany != null) {
                  return false;
               }
            } else if (!this$expressCompany.equals(other$expressCompany)) {
               return false;
            }

            Object this$shipTime = this.getShipTime();
            Object other$shipTime = other.getShipTime();
            if (this$shipTime == null) {
               if (other$shipTime != null) {
                  return false;
               }
            } else if (!this$shipTime.equals(other$shipTime)) {
               return false;
            }

            label167: {
               Object this$refundTime = this.getRefundTime();
               Object other$refundTime = other.getRefundTime();
               if (this$refundTime == null) {
                  if (other$refundTime == null) {
                     break label167;
                  }
               } else if (this$refundTime.equals(other$refundTime)) {
                  break label167;
               }

               return false;
            }

            label160: {
               Object this$receiveTime = this.getReceiveTime();
               Object other$receiveTime = other.getReceiveTime();
               if (this$receiveTime == null) {
                  if (other$receiveTime == null) {
                     break label160;
                  }
               } else if (this$receiveTime.equals(other$receiveTime)) {
                  break label160;
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

            Object this$outTradeNo = this.getOutTradeNo();
            Object other$outTradeNo = other.getOutTradeNo();
            if (this$outTradeNo == null) {
               if (other$outTradeNo != null) {
                  return false;
               }
            } else if (!this$outTradeNo.equals(other$outTradeNo)) {
               return false;
            }

            label139: {
               Object this$payTime = this.getPayTime();
               Object other$payTime = other.getPayTime();
               if (this$payTime == null) {
                  if (other$payTime == null) {
                     break label139;
                  }
               } else if (this$payTime.equals(other$payTime)) {
                  break label139;
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

            Object this$items = this.getItems();
            Object other$items = other.getItems();
            if (this$items == null) {
               if (other$items != null) {
                  return false;
               }
            } else if (!this$items.equals(other$items)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return other instanceof Order;
   }

   @Generated
   public int hashCode() {
      int PRIME = 31;
      int result = 1;
      Object $id = this.getId();
      result = result * 59 + ($id == null ? 43 : $id.hashCode());
      Object $userId = this.getUserId();
      result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
      Object $totalAmount = this.getTotalAmount();
      result = result * 59 + ($totalAmount == null ? 43 : $totalAmount.hashCode());
      Object $addressId = this.getAddressId();
      result = result * 59 + ($addressId == null ? 43 : $addressId.hashCode());
      Object $orderNo = this.getOrderNo();
      result = result * 59 + ($orderNo == null ? 43 : $orderNo.hashCode());
      Object $status = this.getStatus();
      result = result * 59 + ($status == null ? 43 : $status.hashCode());
      Object $shipStatus = this.getShipStatus();
      result = result * 59 + ($shipStatus == null ? 43 : $shipStatus.hashCode());
      Object $refundStatus = this.getRefundStatus();
      result = result * 59 + ($refundStatus == null ? 43 : $refundStatus.hashCode());
      Object $remark = this.getRemark();
      result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
      Object $trackingNumber = this.getTrackingNumber();
      result = result * 59 + ($trackingNumber == null ? 43 : $trackingNumber.hashCode());
      Object $expressCompany = this.getExpressCompany();
      result = result * 59 + ($expressCompany == null ? 43 : $expressCompany.hashCode());
      Object $shipTime = this.getShipTime();
      result = result * 59 + ($shipTime == null ? 43 : $shipTime.hashCode());
      Object $refundTime = this.getRefundTime();
      result = result * 59 + ($refundTime == null ? 43 : $refundTime.hashCode());
      Object $receiveTime = this.getReceiveTime();
      result = result * 59 + ($receiveTime == null ? 43 : $receiveTime.hashCode());
      Object $transactionId = this.getTransactionId();
      result = result * 59 + ($transactionId == null ? 43 : $transactionId.hashCode());
      Object $outTradeNo = this.getOutTradeNo();
      result = result * 59 + ($outTradeNo == null ? 43 : $outTradeNo.hashCode());
      Object $payTime = this.getPayTime();
      result = result * 59 + ($payTime == null ? 43 : $payTime.hashCode());
      Object $createTime = this.getCreateTime();
      result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
      Object $updateTime = this.getUpdateTime();
      result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
      Object $items = this.getItems();
      result = result * 59 + ($items == null ? 43 : $items.hashCode());
      return result;
   }

   @Generated
   public String toString() {
      Long var10000 = this.getId();
      return "Order(id=" + var10000 + ", userId=" + this.getUserId() + ", orderNo=" + this.getOrderNo() + ", totalAmount=" + this.getTotalAmount() + ", status=" + this.getStatus() + ", shipStatus=" + this.getShipStatus() + ", refundStatus=" + this.getRefundStatus() + ", addressId=" + this.getAddressId() + ", remark=" + this.getRemark() + ", trackingNumber=" + this.getTrackingNumber() + ", expressCompany=" + this.getExpressCompany() + ", shipTime=" + this.getShipTime() + ", refundTime=" + this.getRefundTime() + ", receiveTime=" + this.getReceiveTime() + ", transactionId=" + this.getTransactionId() + ", outTradeNo=" + this.getOutTradeNo() + ", payTime=" + this.getPayTime() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", items=" + this.getItems() + ")";
   }
}
