package com.gtu.driver_tracker.infrastructure.client;



import com.gtu.driver_tracker.domain.model.Driver;
import com.gtu.driver_tracker.domain.service.DriverIdentityPort;


public class DriverVerificationFeignAdapter implements DriverIdentityPort {

    private final DriverServiceClient client;

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
           e.printStackTrace();
        }
        return null;
    }
}
