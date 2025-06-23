package com.gtu.driver_tracker.infrastructure.repository;

import com.gtu.driver_tracker.domain.model.Location;
import com.gtu.driver_tracker.domain.repository.DriverLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;





class InMemoryDriverLocationRepositoryImpTest {

    private DriverLocationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryDriverLocationRepositoryImp();
    }

    @Test
    void saveLocationAndGetLocationByDriverId_ShouldWork() {
        Long driverId = 1L;
        Location location = new Location(40.0, -74.0, Instant.now());

        repository.saveLocation(driverId, location);

        Location retrieved = repository.getLocationByDriverId(driverId);
        assertNotNull(retrieved);
        assertEquals(location.getLatitude(), retrieved.getLatitude());
        assertEquals(location.getLongitude(), retrieved.getLongitude());
        assertEquals(location.getTimestamp(), retrieved.getTimestamp());
    }

    @Test
    void getLocationByDriverId_ShouldReturnNullIfNotFound() {
        assertNull(repository.getLocationByDriverId(999L));
    }

    @Test
    void getLastUpdateTimeByDriverId_ShouldReturnTimestampIfExists() {
        Long driverId = 2L;
        Instant now = Instant.now();
        Location location = new Location(51.0, 0.0, now);

        repository.saveLocation(driverId, location);

        Instant lastUpdate = repository.getLastUpdateTimeByDriverId(driverId);
        assertNotNull(lastUpdate);
        assertEquals(now, lastUpdate);
    }

    @Test
    void getLastUpdateTimeByDriverId_ShouldReturnNullIfNotFound() {
        assertNull(repository.getLastUpdateTimeByDriverId(12345L));
    }

    @Test
    void saveLocation_ShouldOverwriteExistingLocation() {
        Long driverId = 3L;
        Location first = new Location(10.0, 20.0, Instant.now());
        Location second = new Location(30.0, 40.0, Instant.now().plusSeconds(60));

        repository.saveLocation(driverId, first);
        repository.saveLocation(driverId, second);

        Location retrieved = repository.getLocationByDriverId(driverId);
        assertNotNull(retrieved);
        assertEquals(second.getLatitude(), retrieved.getLatitude());
        assertEquals(second.getLongitude(), retrieved.getLongitude());
        assertEquals(second.getTimestamp(), retrieved.getTimestamp());
    }
}