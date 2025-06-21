package com.gtu.driver_tracker.infrastructure.repository;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.gtu.driver_tracker.domain.model.Location;
import com.gtu.driver_tracker.domain.repository.DriverLocationRepository;

@Component
public class InMemoryDriverLocationRepositoryImp implements DriverLocationRepository {
     private final Map<Long, Location> locations = new ConcurrentHashMap<>();
    @Override
    public void saveLocation(Long driverId, Location location) {
        locations.put(driverId, location);
    }

    @Override
    public Location getLocationByDriverId(Long driverId) {
        return locations.get(driverId);
    }
    

    @Override
    public Instant getLastUpdateTimeByDriverId(Long driverId) {
        Location location = locations.get(driverId);
        if (location == null) {
            return null;
        }
        return location.getTimestamp();
    }

   
}
