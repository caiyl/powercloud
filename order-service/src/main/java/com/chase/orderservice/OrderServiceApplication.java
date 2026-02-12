package com.chase.orderservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * 订单服务应用启动类
 *
 * @author chase
 * @date 2026/2/12
 */
@SpringBootApplication
@EnableDiscoveryClient  // 启用服务发现（Nacos）
@EnableFeignClients    // 启用Feign客户端
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
        System.out.println("=================================");
        System.out.println("订单服务启动成功!");
        System.out.println("=================================");
    }
}

/**
 * 订单控制器
 */
@RestController
@RefreshScope
class OrderController {

    @Value("${spring.application.name:order-service}")
    private String applicationName;

    @Value("${server.port:8081}")
    private String serverPort;

    // 模拟订单数据
    private static final Map<Long, Map<String, Object>> ORDERS = new HashMap<>();

    static {
        Map<String, Object> order1 = new HashMap<>();
        order1.put("orderId", 1L);
        order1.put("userId", 1L);
        order1.put("productName", "iPhone 15");
        order1.put("price", new BigDecimal("7999.00"));
        order1.put("quantity", 1);
        order1.put("status", "已完成");
        ORDERS.put(1L, order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("orderId", 2L);
        order2.put("userId", 2L);
        order2.put("productName", "MacBook Pro");
        order2.put("price", new BigDecimal("12999.00"));
        order2.put("quantity", 1);
        order2.put("status", "待发货");
        ORDERS.put(2L, order2);
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public String health() {
        return String.format("订单服务 [%s] 正常运行中，端口: %s", applicationName, serverPort);
    }

    /**
     * 获取所有订单
     */
    @GetMapping("/orders")
    public List<Map<String, Object>> getAllOrders() {
        return new ArrayList<>(ORDERS.values());
    }

    /**
     * 根据订单ID获取订单详情
     */
    @GetMapping("/orders/{orderId}")
    public Map<String, Object> getOrderById(@PathVariable Long orderId) {
        return ORDERS.getOrDefault(orderId, Collections.emptyMap());
    }

    /**
     * 根据用户ID获取订单
     */
    @GetMapping("/orders/user/{userId}")
    public List<Map<String, Object>> getOrdersByUserId(@PathVariable Long userId) {
        return ORDERS.values().stream()
                .filter(order -> userId.equals(order.get("userId")))
                .toList();
    }

    /**
     * 创建订单（模拟）
     */
    @GetMapping("/orders/create")
    public Map<String, Object> createOrder() {
        Map<String, Object> newOrder = new HashMap<>();
        long orderId = System.currentTimeMillis();
        newOrder.put("orderId", orderId);
        newOrder.put("userId", 1L);
        newOrder.put("productName", "测试商品");
        newOrder.put("price", new BigDecimal("99.00"));
        newOrder.put("quantity", 1);
        newOrder.put("status", "待支付");
        newOrder.put("createTime", new Date());
        ORDERS.put(orderId, newOrder);
        return newOrder;
    }

    /**
     * 欢迎页面
     */
    @GetMapping("/")
    public String home() {
        return "<h1>欢迎使用订单服务</h1>" +
                "<p>启动时间: " + new Date() + "</p>" +
                "<p>可用接口:</p>" +
                "<ul>" +
                "<li><a href='/health'>/health</a> - 健康检查</li>" +
                "<li><a href='/orders'>/orders</a> - 获取所有订单</li>" +
                "<li><a href='/orders/1'>/orders/{orderId}</a> - 获取指定订单</li>" +
                "<li><a href='/orders/user/1'>/orders/user/{userId}</a> - 获取用户订单</li>" +
                "<li><a href='/orders/create'>/orders/create</a> - 创建订单</li>" +
                "</ul>";
    }
}