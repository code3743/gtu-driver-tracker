package com.gtu.driver_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gtu.driver_tracker.domain.service.DriverIdentityPort;
import com.gtu.driver_tracker.domain.service.TrackingSessionPort;
import com.gtu.driver_tracker.infrastructure.client.DriverServiceClient;
import com.gtu.driver_tracker.infrastructure.client.DriverVerificationFeignAdapter;
import com.gtu.driver_tracker.infrastructure.session.InMemoryTrackingSessionAdapter;

@Configuration
public class BeanConfiguration {

    @Bean
    public DriverIdentityPort driverVerificationPort(DriverServiceClient client) {
        return new DriverVerificationFeignAdapter(client);
    }

    @Bean
    public TrackingSessionPort trackingSessionPort() {
        return new InMemoryTrackingSessionAdapter();
    }
}