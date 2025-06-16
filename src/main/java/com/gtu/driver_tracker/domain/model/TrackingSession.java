package com.gtu.driver_tracker.domain.model;

import java.time.Instant;
/**
 * Represents a driver entity with an identifier and a name.
 * <p>
 * This class provides basic information about a driver, including
 * their unique ID and name. It includes standard getters and setters,
 * as well as a string representation for debugging and logging purposes.
 * </p>
 */
public class TrackingSession {
    private Long sessionId;
    private String driverName;
    private Instant creationTime;

    public TrackingSession(Driver driver) {
        this.sessionId = driver.getId();
        this.driverName = driver.getName();
        this.creationTime = Instant.now();
    }

    public Long getSessionId() {
        return sessionId;
    }

    public String getDriverName() {
        return driverName;
    }


    public Instant getCreationTime() {
        return creationTime;
    }

}
