package com.gtu.driver_tracker.domain.model;

import java.time.Instant;
import java.util.UUID;
/**
 * Represents a driver entity with an identifier and a name.
 * <p>
 * This class provides basic information about a driver, including
 * their unique ID and name. It includes standard getters and setters,
 * as well as a string representation for debugging and logging purposes.
 * </p>
 */
public class TrackingSession {
    private Long driverId; 
    private UUID sessionId;
    private String driverName;
    private Instant creationTime;

    public TrackingSession(Driver driver) {
        this.driverId = driver.getId();
        this.driverName = driver.getName();
        this.creationTime = Instant.now();
        this.sessionId = UUID.randomUUID();
    }

    public Long getDriverId() {
        return driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

}
