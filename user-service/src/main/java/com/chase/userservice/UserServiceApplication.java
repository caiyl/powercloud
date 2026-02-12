package com.chase.userservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

import java.util.HashMap;
import java.util.Map;

/**
 * PowerCloud 微服务应用启动类
 *
 * @author chase
 * @date 2026/2/11 14:51
 */
@SpringBootApplication
@EnableDiscoveryClient  // 启用服务发现（Nacos）
public class UserServiceApplication {

    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("=================================");
        System.out.println("用户服务启动成功!");
        System.out.println("=================================");
    }
}

/**
 * 用户服务控制器
 */
@RestController
@RefreshScope  // 开启动态刷新
class TestController {

    @Value("${spring.application.name:user-service}")
    private String applicationName;

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${user.name:未知用户}")
    private String userName;

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public String health() {
        return String.format("应用 [%s] 正常运行中，端口: %s", applicationName, serverPort);
    }

    /**
     * 配置测试接口
     */
    @GetMapping("/config")
    public Map<String, String> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("application.name", applicationName);
        config.put("server.port", serverPort);
        config.put("user.name", userName);
        config.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return config;
    }

    // 模拟用户数据
    private static final Map<Long, Map<String, Object>> USERS = new HashMap<>();

    static {
        Map<String, Object> user1 = new HashMap<>();
        user1.put("userId", 1L);
        user1.put("username", "张三");
        user1.put("email", "zhangsan@example.com");
        user1.put("phone", "13800138001");
        user1.put("status", "active");
        USERS.put(1L, user1);

        Map<String, Object> user2 = new HashMap<>();
        user2.put("userId", 2L);
        user2.put("username", "李四");
        user2.put("email", "lisi@example.com");
        user2.put("phone", "13800138002");
        user2.put("status", "active");
        USERS.put(2L, user2);
    }

    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping("/users/{userId}")
    public Map<String, Object> getUserById(@PathVariable("userId") Long userId) {
        return USERS.getOrDefault(userId, Collections.emptyMap());
    }

    /**
     * 验证用户是否存在
     */
    @GetMapping("/users/{userId}/exists")
    public boolean checkUserExists(@PathVariable("userId") Long userId) {
        return USERS.containsKey(userId);
    }

    /**
     * 欢迎页面
     */
    @GetMapping("/")
    public String home() {
        return "<h1>欢迎使用用户服务</h1>" +
                "<p>启动时间: " + new java.util.Date() + "</p>" +
                "<p>可用接口:</p>" +
                "<ul>" +
                "<li><a href='/health'>/health</a> - 健康检查</li>" +
                "<li><a href='/config'>/config</a> - 配置信息</li>" +
                "<li><a href='/users/1'>/users/{userId}</a> - 获取用户信息</li>" +
                "<li><a href='/users/1/exists'>/users/{userId}/exists</a> - 验证用户存在</li>" +
                "</ul>";
    }
}