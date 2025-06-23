package com.gtu.driver_tracker.infrastructure.client;


import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.gtu.driver_tracker.domain.model.Driver;
import com.gtu.driver_tracker.domain.service.DriverIdentityPort;

@Service
public class DriverVerificationFeignAdapter implements DriverIdentityPort {

    private final DriverServiceClient client;
    private final Logger log = Logger.getLogger(DriverVerificationFeignAdapter.class.getName());

    public DriverVerificationFeignAdapter(DriverServiceClient client) {
        this.client = client;
    }

    @Override
    public Driver getDriverById(Long driverId) {
        try {
            var user = client.getUserById(driverId);
            if (user != null && "DRIVER".equalsIgnoreCase(user.data().role())) {
                return new Driver(user.data().id(), user.data().name());
            }
        } catch (Exception e) {
            log.info("Error fetching driver by ID: " + driverId);
        }
        return null;
    }
}
