package com.gtu.driver_tracker.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gtu-users-management-service")
public interface DriverServiceClient {

    @GetMapping("/users/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);

    record UserResponse(Long id, String name, String email, String role) {
        public String getRole() {
            return role != null ? role.toUpperCase() : null;
        }
    }
}
