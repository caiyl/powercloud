package com.chase.powercloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class App {

    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(App.class, args);
        System.out.println("=================================");
        System.out.println("PowerCloud 应用启动成功!");
        System.out.println("=================================");
    }
}

/**
 * 测试控制器，验证 Nacos 配置中心是否正常工作
 */
@RestController
@RefreshScope  // 开启动态刷新
class TestController {

    @Value("${spring.application.name:powercloud}")
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

    /**
     * 欢迎页面
     */
    @GetMapping("/")
    public String home() {
        return "<h1>欢迎使用 PowerCloud 微服务</h1>" +
                "<p>启动时间: " + new java.util.Date() + "</p>" +
                "<p>更多接口: <a href='/health'>/health</a>, <a href='/config'>/config</a></p>";
    }
}