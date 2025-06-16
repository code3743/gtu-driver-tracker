package com.gtu.driver_tracker.domain.service;

import com.gtu.driver_tracker.domain.model.Driver;

public interface DriverIdentityPort {

    /**
     * Retrieves the driver by their ID.
     *
     * @param driverId the ID of the driver to retrieve
     * @return the Driver object if found, null otherwise
     */
    Driver getDriverById(Long driverId);
    
}
