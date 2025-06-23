package com.gtu.driver_tracker.domain.service;

import java.util.UUID;

import com.gtu.driver_tracker.domain.exception.GeneralException;
import com.gtu.driver_tracker.domain.model.Location;

public interface LocationService {
    /**
     * Sends the location of a driver to users
     * @param driverId The ID of the driver.
     * @param latitude The latitude of the driver's location.
     * @param longitude The longitude of the driver's location.
     */
    void updateLocation(Long driverId, double latitude, double longitude) throws GeneralException;

    /**
     * Notifies the driver about a change in their assigned system
     *
     * @param driverId The ID of the driver.
     * @param sessionId The session ID associated with the driver's tracking session.
     * @param location The new location of the driver.
     */
    void notifyDriverLocationChange(Long driverId, UUID sessionId, Location location);


    /**
     * Notifies the driver about an error that occurred during tracking.
     *
     * @param driverId The ID of the driver.
     * @param exception The exception that occurred.
     */
    void notifyDriverError(Long driverId, GeneralException exception);
}
