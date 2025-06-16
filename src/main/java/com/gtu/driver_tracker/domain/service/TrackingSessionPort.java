package com.gtu.driver_tracker.domain.service;


import com.gtu.driver_tracker.domain.model.TrackingSession;

public interface  TrackingSessionPort {

    /**
     * Starts a new tracking session for the given driver.
     *
     * @param driverId the ID of the driver
     * @return the ID of the newly created tracking session
     */
    void startTrackingSession(Long driverId);

    /**
     * Ends the tracking session for the given driver.
     *
     * @param sessionId the ID of the tracking session to end
     */
    void endTrackingSession(Long sessionId);


    /**
     * Retrieves the tracking session by its ID.
     *
     * @param sessionId the ID of the tracking session
     * @return an Optional containing the TrackingSession if found, or empty if not found
     */
    TrackingSession getTrackingSessionById(Long sessionId);

    /**
     * Checks if the driver is currently being tracked.
     *
     * @param driverId the ID of the driver to check
     * @return true if the driver is being tracked, false otherwise
     */
    boolean isTracking(Long driverId);
}
