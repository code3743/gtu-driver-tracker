package com.gtu.driver_tracker.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gtu.driver_tracker.infrastructure.dto.ApiResponse;
import com.gtu.driver_tracker.infrastructure.dto.UserResponse;



@FeignClient(name = "gtu-users-management-service")
public interface DriverServiceClient {

    @GetMapping("/users/{id}")
    ApiResponse<UserResponse> getUserById(@PathVariable("id") Long id);

}
