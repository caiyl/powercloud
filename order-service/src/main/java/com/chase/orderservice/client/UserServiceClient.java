package com.chase.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 用户服务Feign客户端
 * 用于订单服务调用用户服务
 *
 * @author chase
 * @date 2026/2/12
 */
@FeignClient(name = "user-service")
public interface UserServiceClient {

    /**
     * 根据用户ID获取用户信息
     * 
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/users/{userId}")
    Map<String, Object> getUserById(@PathVariable("userId") Long userId);

    /**
     * 验证用户是否存在
     * 
     * @param userId 用户ID
     * @return 是否存在
     */
    @GetMapping("/users/{userId}/exists")
    boolean checkUserExists(@PathVariable("userId") Long userId);

    /**
     * 获取用户配置信息
     * 
     * @return 用户配置
     */
    @GetMapping("/config")
    Map<String, String> getUserConfig();
}