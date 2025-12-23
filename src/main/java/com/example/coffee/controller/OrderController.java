package com.example.coffee.controller;

import com.example.coffee.common.Result;
import com.example.coffee.entity.Order;
import com.example.coffee.entity.OrderItem;
import com.example.coffee.service.OrderService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/order"})
@CrossOrigin
public class OrderController {
   @Autowired
   private OrderService orderService;

   @PostMapping({"/create"})
   public Result createOrder(@RequestBody Map<String, Object> request) {
      try {
         Long userId = ((Number)request.get("userId")).longValue();
         Long addressId = ((Number)request.get("addressId")).longValue();
         String productName = (String)request.get("productName");
         Double productPrice = ((Number)request.get("productPrice")).doubleValue();
         Integer quantity = ((Number)request.get("quantity")).intValue();
         Double totalPrice = ((Number)request.get("totalPrice")).doubleValue();
         OrderItem item = new OrderItem();
         item.setProductName(productName);
         item.setProductPrice(productPrice);
         item.setQuantity(quantity);
         item.setTotalPrice(totalPrice);
         List<OrderItem> items = new ArrayList();
         items.add(item);
         Order order = this.orderService.createOrder(userId, addressId, items, totalPrice);
         return Result.success("订单创建成功", order);
      } catch (Exception var11) {
         return Result.error("订单创建失败: " + var11.getMessage());
      }
   }

   @PostMapping({"/createPending"})
   public Result createPendingOrder(@RequestBody Map<String, Object> request) {
      try {
         Long userId = ((Number)request.get("userId")).longValue();
         Long addressId = ((Number)request.get("addressId")).longValue();
         String productName = (String)request.get("productName");
         Double productPrice = ((Number)request.get("productPrice")).doubleValue();
         Integer quantity = ((Number)request.get("quantity")).intValue();
         Double totalPrice = ((Number)request.get("totalPrice")).doubleValue();
         OrderItem item = new OrderItem();
         item.setProductName(productName);
         item.setProductPrice(productPrice);
         item.setQuantity(quantity);
         item.setTotalPrice(totalPrice);
         List<OrderItem> items = new ArrayList();
         items.add(item);
         Order order = this.orderService.createPendingOrder(userId, addressId, items, totalPrice);
         return Result.success("订单创建成功", order);
      } catch (Exception var11) {
         return Result.error("订单创建失败: " + var11.getMessage());
      }
   }

   @GetMapping({"/list/{userId}"})
   public Result getOrdersByUserId(@PathVariable Long userId) {
      try {
         List<Order> orders = this.orderService.getOrdersByUserId(userId);
         return Result.success("获取成功", orders);
      } catch (Exception var3) {
         return Result.error("获取失败: " + var3.getMessage());
      }
   }

   @GetMapping({"/list/{userId}/status"})
   public Result getOrdersByStatus(@PathVariable Long userId, @RequestParam String status) {
      try {
         List<Order> orders = this.orderService.getOrdersByUserIdAndStatus(userId, status);
         return Result.success("获取成功", orders);
      } catch (Exception var4) {
         return Result.error("获取失败: " + var4.getMessage());
      }
   }

   @GetMapping({"/list/{userId}/shipStatus"})
   public Result getOrdersByShipStatus(@PathVariable Long userId, @RequestParam String shipStatus) {
      try {
         List<Order> orders = this.orderService.getOrdersByUserIdAndShipStatus(userId, shipStatus);
         return Result.success("获取成功", orders);
      } catch (Exception var4) {
         return Result.error("获取失败: " + var4.getMessage());
      }
   }

   @GetMapping({"/list/{userId}/refundStatus"})
   public Result getOrdersByRefundStatus(@PathVariable Long userId, @RequestParam String refundStatus) {
      try {
         List<Order> orders = this.orderService.getOrdersByUserIdAndRefundStatus(userId, refundStatus);
         return Result.success("获取成功", orders);
      } catch (Exception var4) {
         return Result.error("获取失败: " + var4.getMessage());
      }
   }

   @GetMapping({"/{orderId}"})
   public Result getOrderById(@PathVariable Long orderId) {
      try {
         Order order = this.orderService.getOrderById(orderId);
         return order == null ? Result.error("订单不存在") : Result.success("获取成功", order);
      } catch (Exception var3) {
         return Result.error("获取失败: " + var3.getMessage());
      }
   }

   @DeleteMapping({"/{orderId}"})
   public Result cancelOrder(@PathVariable Long orderId) {
      try {
         this.orderService.cancelOrder(orderId);
         return Result.success("订单已取消", (Object)null);
      } catch (Exception var3) {
         return Result.error("取消失败: " + var3.getMessage());
      }
   }

   @PutMapping({"/{orderId}/pay"})
   public Result payOrder(@PathVariable Long orderId) {
      try {
         this.orderService.payOrder(orderId);
         return Result.success("支付成功", (Object)null);
      } catch (Exception var3) {
         return Result.error("支付失败: " + var3.getMessage());
      }
   }

   @GetMapping({"/admin/all"})
   public Result getAllOrders() {
      try {
         List<Order> orders = this.orderService.getAllOrders();
         return Result.success("获取成功", orders);
      } catch (Exception var2) {
         return Result.error("获取失败: " + var2.getMessage());
      }
   }

   @PutMapping({"/{orderId}/ship"})
   public Result shipOrder(@PathVariable Long orderId, @RequestBody Map<String, String> request) {
      try {
         String trackingNumber = (String)request.get("trackingNumber");
         String expressCompany = (String)request.get("expressCompany");
         if (trackingNumber != null && !trackingNumber.trim().isEmpty()) {
            if (expressCompany != null && !expressCompany.trim().isEmpty()) {
               this.orderService.shipOrder(orderId, trackingNumber, expressCompany);
               return Result.success("发货成功", (Object)null);
            } else {
               return Result.error("快递公司不能为空");
            }
         } else {
            return Result.error("物流单号不能为空");
         }
      } catch (Exception var5) {
         return Result.error("发货失败: " + var5.getMessage());
      }
   }
}
