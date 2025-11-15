package com.example.coffee.service;

import com.example.coffee.entity.Order;
import com.example.coffee.entity.OrderItem;
import com.example.coffee.entity.User;
import com.example.coffee.repository.OrderRepository;
import com.example.coffee.repository.OrderItemRepository;
import com.example.coffee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 创建订单（已支付状态）
     */
    @Transactional
    public Order createOrder(Long userId, Long addressId, List<OrderItem> items, Double totalAmount) {
        // 验证用户存在
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setOrderNo(generateOrderNo());  // 生成订单号
        order.setTotalAmount(totalAmount);
        order.setStatus("已支付");  // 支付状态：已支付
        order.setShipStatus("待发货");  // 发货状态：待发货
        order.setRefundStatus("无");  // 退款状态：无
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        
        // 保存订单
        Order savedOrder = orderRepository.save(order);
        
        // 保存订单项目
        for (OrderItem item : items) {
            item.setOrderId(savedOrder.getId());
            orderItemRepository.save(item);
        }
        
        // 更新用户积分和消费金额
        // 积分 = 购买金额 × 会员折扣返现比例 ÷ 100
        Integer discountRate = calculateDiscountRate(user.getPoints());
        Integer earnedPoints = (int) (totalAmount * discountRate / 100);
        user.setPoints(user.getPoints() + earnedPoints);
        user.setConsumption(user.getConsumption() + totalAmount.longValue());
        userRepository.save(user);
        
        return savedOrder;
    }
    
    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + new Random().nextInt(1000);
    }
    
    /**
     * 根据用户积分计算折扣率
     */
    private Integer calculateDiscountRate(Integer points) {
        if (points < 1000) {
            return 0;  // 初级者 0%
        } else if (points < 10000) {
            return 10;  // 探索者 10%
        } else if (points < 50000) {
            return 20;  // 品鉴家 20%
        } else if (points < 100000) {
            return 30;  // 珍藏家 30%
        } else {
            return 40;  // 共创者 40%
        }
    }
    
    /**
     * 获取用户订单列表
     */
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
    
    /**
     * 按支付状态查询订单
     */
    public List<Order> getOrdersByUserIdAndStatus(Long userId, String status) {
        return orderRepository.findByUserIdAndStatus(userId, status);
    }
    
    /**
     * 按发货状态查询订单
     */
    public List<Order> getOrdersByUserIdAndShipStatus(Long userId, String shipStatus) {
        return orderRepository.findByUserIdAndShipStatus(userId, shipStatus);
    }
    
    /**
     * 按退款状态查询订单
     */
    public List<Order> getOrdersByUserIdAndRefundStatus(Long userId, String refundStatus) {
        return orderRepository.findByUserIdAndRefundStatus(userId, refundStatus);
    }
    
    /**
     * 获取订单详情
     */
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    /**
     * 创建待支付订单
     */
    @Transactional
    public Order createPendingOrder(Long userId, Long addressId, List<OrderItem> items, Double totalAmount) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        Order order = new Order();
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setOrderNo(generateOrderNo());
        order.setTotalAmount(totalAmount);
        order.setStatus("待支付");  // 待支付状态
        order.setShipStatus("待发货");
        order.setRefundStatus("无");
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        
        Order savedOrder = orderRepository.save(order);
        
        // 保存订单项目
        for (OrderItem item : items) {
            item.setOrderId(savedOrder.getId());
            orderItemRepository.save(item);
        }
        
        return savedOrder;
    }

    /**
     * 取消订单（删除待支付订单）
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        if (!"待支付".equals(order.getStatus())) {
            throw new RuntimeException("只能取消待支付的订单");
        }
        
        // 删除订单项目
        orderItemRepository.deleteByOrderId(orderId);
        
        // 删除订单
        orderRepository.deleteById(orderId);
    }

    /**
     * 支付订单（更新状态为已支付）
     */
    @Transactional
    public void payOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        if (!"待支付".equals(order.getStatus())) {
            throw new RuntimeException("只能支付待支付状态的订单");
        }
        
        order.setStatus("已支付");
        order.setUpdateTime(new Date());
        orderRepository.save(order);
    }

    /**
     * 获取所有订单（管理员使用）
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * 发货（更新物流单号、快递公司、发货时间和发货状态）
     */
    @Transactional
    public void shipOrder(Long orderId, String trackingNumber, String expressCompany) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        if (!"已支付".equals(order.getStatus())) {
            throw new RuntimeException("只能对已支付的订单发货");
        }
        
        order.setTrackingNumber(trackingNumber);
        order.setExpressCompany(expressCompany);
        order.setShipTime(new Date());
        order.setShipStatus("已发货");
        order.setUpdateTime(new Date());
        orderRepository.save(order);
    }
}