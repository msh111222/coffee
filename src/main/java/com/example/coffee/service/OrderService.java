package com.example.coffee.service;

import com.example.coffee.entity.Order;
import com.example.coffee.entity.OrderItem;
import com.example.coffee.entity.User;
import com.example.coffee.repository.OrderItemRepository;
import com.example.coffee.repository.OrderRepository;
import com.example.coffee.repository.UserRepository;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
   @Autowired
   private OrderRepository orderRepository;
   @Autowired
   private OrderItemRepository orderItemRepository;
   @Autowired
   private UserRepository userRepository;

   @Transactional
   public Order createOrder(Long userId, Long addressId, List<OrderItem> items, Double totalAmount) {
      User user = (User)this.userRepository.findById(userId).orElse(null);
      if (user == null) {
         throw new RuntimeException("用户不存在");
      } else {
         Order order = new Order();
         order.setUserId(userId);
         order.setAddressId(addressId);
         order.setOrderNo(this.generateOrderNo());
         order.setTotalAmount(totalAmount);
         order.setStatus("已支付");
         order.setShipStatus("待发货");
         order.setRefundStatus("无");
         order.setCreateTime(new Date());
         order.setUpdateTime(new Date());
         Order savedOrder = (Order)this.orderRepository.save(order);
         Iterator var8 = items.iterator();

         while(var8.hasNext()) {
            OrderItem item = (OrderItem)var8.next();
            item.setOrderId(savedOrder.getId());
            this.orderItemRepository.save(item);
         }

         Integer discountRate = this.calculateDiscountRate(user.getPoints());
         Integer earnedPoints = (int)(totalAmount * (double)discountRate / 100.0D);
         user.setPoints(user.getPoints() + earnedPoints);
         user.setConsumption(user.getConsumption() + totalAmount.longValue());
         this.userRepository.save(user);
         return savedOrder;
      }
   }

   private String generateOrderNo() {
      long var10000 = System.currentTimeMillis();
      return "ORD" + var10000 + (new Random()).nextInt(1000);
   }

   private Integer calculateDiscountRate(Integer points) {
      if (points < 1000) {
         return 0;
      } else if (points < 10000) {
         return 10;
      } else if (points < 50000) {
         return 20;
      } else {
         return points < 100000 ? 30 : 40;
      }
   }

   public List<Order> getOrdersByUserId(Long userId) {
      return this.orderRepository.findByUserId(userId);
   }

   public List<Order> getOrdersByUserIdAndStatus(Long userId, String status) {
      return this.orderRepository.findByUserIdAndStatus(userId, status);
   }

   public List<Order> getOrdersByUserIdAndShipStatus(Long userId, String shipStatus) {
      return this.orderRepository.findByUserIdAndShipStatus(userId, shipStatus);
   }

   public List<Order> getOrdersByUserIdAndRefundStatus(Long userId, String refundStatus) {
      return this.orderRepository.findByUserIdAndRefundStatus(userId, refundStatus);
   }

   public Order getOrderById(Long orderId) {
      return (Order)this.orderRepository.findById(orderId).orElse(null);
   }

   @Transactional
   public Order createPendingOrder(Long userId, Long addressId, List<OrderItem> items, Double totalAmount) {
      User user = (User)this.userRepository.findById(userId).orElse(null);
      if (user == null) {
         throw new RuntimeException("用户不存在");
      } else {
         Order order = new Order();
         order.setUserId(userId);
         order.setAddressId(addressId);
         order.setOrderNo(this.generateOrderNo());
         order.setTotalAmount(totalAmount);
         order.setStatus("待支付");
         order.setShipStatus("待发货");
         order.setRefundStatus("无");
         order.setCreateTime(new Date());
         order.setUpdateTime(new Date());
         Order savedOrder = (Order)this.orderRepository.save(order);
         Iterator var8 = items.iterator();

         while(var8.hasNext()) {
            OrderItem item = (OrderItem)var8.next();
            item.setOrderId(savedOrder.getId());
            this.orderItemRepository.save(item);
         }

         return savedOrder;
      }
   }

   @Transactional
   public void cancelOrder(Long orderId) {
      Order order = (Order)this.orderRepository.findById(orderId).orElse(null);
      if (order == null) {
         throw new RuntimeException("订单不存在");
      } else if (!"待支付".equals(order.getStatus())) {
         throw new RuntimeException("只能取消待支付的订单");
      } else {
         this.orderItemRepository.deleteByOrderId(orderId);
         this.orderRepository.deleteById(orderId);
      }
   }

   @Transactional
   public void payOrder(Long orderId) {
      Order order = (Order)this.orderRepository.findById(orderId).orElse(null);
      if (order == null) {
         throw new RuntimeException("订单不存在");
      } else if (!"待支付".equals(order.getStatus())) {
         throw new RuntimeException("只能支付待支付状态的订单");
      } else {
         order.setStatus("已支付");
         order.setUpdateTime(new Date());
         this.orderRepository.save(order);
      }
   }

   public List<Order> getAllOrders() {
      return this.orderRepository.findAll();
   }

   @Transactional
   public void shipOrder(Long orderId, String trackingNumber, String expressCompany) {
      Order order = (Order)this.orderRepository.findById(orderId).orElse(null);
      if (order == null) {
         throw new RuntimeException("订单不存在");
      } else if (!"已支付".equals(order.getStatus())) {
         throw new RuntimeException("只能对已支付的订单发货");
      } else {
         order.setTrackingNumber(trackingNumber);
         order.setExpressCompany(expressCompany);
         order.setShipTime(new Date());
         order.setShipStatus("已发货");
         order.setUpdateTime(new Date());
         this.orderRepository.save(order);
      }
   }
}
