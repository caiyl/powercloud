package com.chase.userservice;

import com.chase.userservice.api.UserServiceFeign;
import com.chase.userservice.dto.UserInfoDTO;
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
 * PowerCloud 用户服务应用启动类
 *
 * @author chase
 * @date 2026/2/12
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

