package com.example.coffee.controller;

import com.example.coffee.entity.Order;
import com.example.coffee.entity.OrderItem;
import com.example.coffee.service.OrderService;
import com.example.coffee.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;
    
    /**
     * 创建订单（已支付状态）
     */
    @PostMapping("/create")
    public Result createOrder(@RequestBody Map<String, Object> request) {
        try {
            Long userId = ((Number) request.get("userId")).longValue();
            Long addressId = ((Number) request.get("addressId")).longValue();
            String productName = (String) request.get("productName");
            Double productPrice = ((Number) request.get("productPrice")).doubleValue();
            Integer quantity = ((Number) request.get("quantity")).intValue();
            Double totalPrice = ((Number) request.get("totalPrice")).doubleValue();
            
            // 创建订单项目
            OrderItem item = new OrderItem();
            item.setProductName(productName);
            item.setProductPrice(productPrice);
            item.setQuantity(quantity);
            item.setTotalPrice(totalPrice);
            
            List<OrderItem> items = new ArrayList<>();
            items.add(item);
            
            // 创建订单
            Order order = orderService.createOrder(userId, addressId, items, totalPrice);
            
            return Result.success("订单创建成功", order);
        } catch (Exception e) {
            return Result.error("订单创建失败: " + e.getMessage());
        }
    }

    /**
     * 创建待支付订单
     */
    @PostMapping("/createPending")
    public Result createPendingOrder(@RequestBody Map<String, Object> request) {
        try {
            Long userId = ((Number) request.get("userId")).longValue();
            Long addressId = ((Number) request.get("addressId")).longValue();
            String productName = (String) request.get("productName");
            Double productPrice = ((Number) request.get("productPrice")).doubleValue();
            Integer quantity = ((Number) request.get("quantity")).intValue();
            Double totalPrice = ((Number) request.get("totalPrice")).doubleValue();
            
            // 创建订单项目
            OrderItem item = new OrderItem();
            item.setProductName(productName);
            item.setProductPrice(productPrice);
            item.setQuantity(quantity);
            item.setTotalPrice(totalPrice);
            
            List<OrderItem> items = new ArrayList<>();
            items.add(item);
            
            // 创建待支付订单
            Order order = orderService.createPendingOrder(userId, addressId, items, totalPrice);
            
            return Result.success("订单创建成功", order);
        } catch (Exception e) {
            return Result.error("订单创建失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户订单列表
     */
    @GetMapping("/list/{userId}")
    public Result getOrdersByUserId(@PathVariable Long userId) {
        try {
            List<Order> orders = orderService.getOrdersByUserId(userId);
            return Result.success("获取成功", orders);
        } catch (Exception e) {
            return Result.error("获取失败: " + e.getMessage());
        }
    }
    
    /**
     * 按支付状态查询订单
     */
    @GetMapping("/list/{userId}/status")
    public Result getOrdersByStatus(@PathVariable Long userId, @RequestParam String status) {
        try {
            List<Order> orders = orderService.getOrdersByUserIdAndStatus(userId, status);
            return Result.success("获取成功", orders);
        } catch (Exception e) {
            return Result.error("获取失败: " + e.getMessage());
        }
    }
    
    /**
     * 按发货状态查询订单
     */
    @GetMapping("/list/{userId}/shipStatus")
    public Result getOrdersByShipStatus(@PathVariable Long userId, @RequestParam String shipStatus) {
        try {
            List<Order> orders = orderService.getOrdersByUserIdAndShipStatus(userId, shipStatus);
            return Result.success("获取成功", orders);
        } catch (Exception e) {
            return Result.error("获取失败: " + e.getMessage());
        }
    }
    
    /**
     * 按退款状态查询订单
     */
    @GetMapping("/list/{userId}/refundStatus")
    public Result getOrdersByRefundStatus(@PathVariable Long userId, @RequestParam String refundStatus) {
        try {
            List<Order> orders = orderService.getOrdersByUserIdAndRefundStatus(userId, refundStatus);
            return Result.success("获取成功", orders);
        } catch (Exception e) {
            return Result.error("获取失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取订单详情
     */
    @GetMapping("/{orderId}")
    public Result getOrderById(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                return Result.error("订单不存在");
            }
            return Result.success("获取成功", order);
        } catch (Exception e) {
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 取消订单（删除待支付订单）
     */
    @DeleteMapping("/{orderId}")
    public Result cancelOrder(@PathVariable Long orderId) {
        try {
            orderService.cancelOrder(orderId);
            return Result.success("订单已取消", null);
        } catch (Exception e) {
            return Result.error("取消失败: " + e.getMessage());
        }
    }

    /**
     * 支付订单（更新订单状态为已支付）
     */
    @PutMapping("/{orderId}/pay")
    public Result payOrder(@PathVariable Long orderId) {
        try {
            orderService.payOrder(orderId);
            return Result.success("支付成功", null);
        } catch (Exception e) {
            return Result.error("支付失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有订单（管理员使用）
     */
    @GetMapping("/admin/all")
    public Result getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders();
            return Result.success("获取成功", orders);
        } catch (Exception e) {
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 发货（录入物流单号和快递公司）
     */
    @PutMapping("/{orderId}/ship")
    public Result shipOrder(@PathVariable Long orderId, @RequestBody Map<String, String> request) {
        try {
            String trackingNumber = request.get("trackingNumber");
            String expressCompany = request.get("expressCompany");
            
            if (trackingNumber == null || trackingNumber.trim().isEmpty()) {
                return Result.error("物流单号不能为空");
            }
            if (expressCompany == null || expressCompany.trim().isEmpty()) {
                return Result.error("快递公司不能为空");
            }
            
            orderService.shipOrder(orderId, trackingNumber, expressCompany);
            return Result.success("发货成功", null);
        } catch (Exception e) {
            return Result.error("发货失败: " + e.getMessage());
        }
    }
}