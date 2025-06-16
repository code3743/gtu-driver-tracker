package com.gtu.driver_tracker.infrastructure.client;

import com.gtu.driver_tracker.domain.service.DriverVerificationPort;


public class DriverVerificationFeignAdapter implements DriverVerificationPort {

    private final DriverServiceClient client;

    public DriverVerificationFeignAdapter(DriverServiceClient client) {
        this.client = client;
    }



    @Override
    public boolean verifyDriver(Long driverId) {
        try {
            DriverServiceClient.UserResponse user = client.getUserById(driverId);
            return user != null && "DRIVER".equalsIgnoreCase(user.getRole());
        } catch (Exception e) {
            return false;
        }
    }
}
