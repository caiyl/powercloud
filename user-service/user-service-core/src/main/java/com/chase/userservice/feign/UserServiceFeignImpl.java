package com.chase.userservice.feign;

import com.chase.userservice.api.UserServiceFeign;
import com.chase.userservice.dto.UserInfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务Feign接口实现类
 * 实现UserServiceFeign接口定义的所有方法
 *
 * @author chase
 * @date 2026/2/12
 */
@RestController
@RefreshScope  // 开启动态刷新
public class UserServiceFeignImpl implements UserServiceFeign {

    @Value("${spring.application.name:user-service}")
    private String applicationName;

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${user.name:未知用户}")
    private String userName;

    // 模拟用户数据
    private static final Map<Long, UserInfoDTO> USERS = new HashMap<>();

    static {
        // 初始化测试用户数据
        UserInfoDTO user1 = new UserInfoDTO();
        user1.setUserId(1L);
        user1.setUsername("张三");
        user1.setEmail("zhangsan@example.com");
        user1.setPhone("13800138001");
        user1.setStatus(1);
        user1.setCreateTime(LocalDateTime.now());
        user1.setUpdateTime(LocalDateTime.now());
        USERS.put(1L, user1);

        UserInfoDTO user2 = new UserInfoDTO();
        user2.setUserId(2L);
        user2.setUsername("李四");
        user2.setEmail("lisi@example.com");
        user2.setPhone("13800138002");
        user2.setStatus(1);
        user2.setCreateTime(LocalDateTime.now());
        user2.setUpdateTime(LocalDateTime.now());
        USERS.put(2L, user2);
    }

    /**
     * 根据用户ID获取用户信息
     * 实现UserServiceFeign接口方法
     */
    @Override
    public UserInfoDTO getUserById(@PathVariable("userId") Long userId) {
        return USERS.getOrDefault(userId, new UserInfoDTO());
    }

    /**
     * 验证用户是否存在
     * 实现UserServiceFeign接口方法
     */
    @Override
    public boolean checkUserExists(@PathVariable("userId") Long userId) {
        return USERS.containsKey(userId);
    }

    /**
     * 获取用户配置信息
     * 实现UserServiceFeign接口方法
     */
    @Override
    public Map<String, String> getUserConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("application.name", applicationName);
        config.put("server.port", serverPort);
        config.put("user.name", userName);
        config.put("maxOrdersPerDay", "10");
        config.put("vipLevel", "gold");
        config.put("discountRate", "0.9");
        return config;
    }

    /**
     * 健康检查接口（额外功能，不在Feign接口中）
     */

    public String health() {
        return String.format("应用 [%s] 正常运行中，端口: %s", applicationName, serverPort);
    }

    /**
     * 配置测试接口（额外功能，不在Feign接口中）
     */

    public Map<String, String> getConfigDetail() {
        Map<String, String> config = new HashMap<>();
        config.put("application.name", applicationName);
        config.put("server.port", serverPort);
        config.put("user.name", userName);
        config.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return config;
    }

    /**
     * 欢迎页面（额外功能，不在Feign接口中）
     */

    public String home() {
        return "<h1>欢迎使用用户服务</h1>" +
                "<p>启动时间: " + new java.util.Date() + "</p>" +
                "<p>可用接口:</p>" +
                "<ul>" +
                "<li><a href='/health'>/health</a> - 健康检查</li>" +
                "<li><a href='/config/detail'>/config/detail</a> - 配置信息</li>" +
                "<li><a href='/users/1'>/users/{userId}</a> - 获取用户信息</li>" +
                "<li><a href='/users/1/exists'>/users/{userId}/exists</a> - 验证用户存在</li>" +
                "<li><a href='/config'>/config</a> - 用户配置</li>" +
                "</ul>";
    }
}