package com.gtu.driver_tracker.domain.repository;

import java.time.Instant;

import com.gtu.driver_tracker.domain.model.Location;

public interface DriverLocationRepository {

    void saveLocation(Long driverId, Location location);
    Location getLocationByDriverId(Long driverId);
    Instant getLastUpdateTimeByDriverId(Long driverId);
}
