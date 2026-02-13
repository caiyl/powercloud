package com.chase.userservice.api;

import com.chase.userservice.dto.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * 用户服务Feign客户端接口
 * 定义用户服务对外暴露的API
 *
 * @author chase
 * @date 2026/2/12
 */
@FeignClient(name = "user-service")
public interface UserServiceFeign {

    /**
     * 根据用户ID获取用户信息
     * 
     * @param userId 用户ID
     * @return 用户信息DTO
     */
    @GetMapping("/users/{userId}")
    UserInfoDTO getUserById(@PathVariable("userId") Long userId);

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