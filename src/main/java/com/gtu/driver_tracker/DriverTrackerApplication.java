package com.gtu.driver_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.gtu.driver_tracker.infrastructure.client")
public class DriverTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DriverTrackerApplication.class, args);
	}

}
