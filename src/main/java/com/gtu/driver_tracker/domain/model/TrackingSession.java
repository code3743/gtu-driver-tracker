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
    private Instant creationTime;

    public TrackingSession(Long driverId) {
        this.sessionId = driverId;
        this.creationTime = Instant.now();
    }

    public Long getSessionId() {
        return sessionId;
    }


    public Instant getCreationTime() {
        return creationTime;
    }

}
