package com.gtu.driver_tracker.application.usecase;

import org.springframework.stereotype.Component;

import com.gtu.driver_tracker.domain.exception.GeneralException;
import com.gtu.driver_tracker.domain.repository.TrackingSessionRepository;

@Component
public class StopTrackingUseCase {
    private final TrackingSessionRepository trackingSessionPort;

    public StopTrackingUseCase(TrackingSessionRepository trackingSessionPort) {
        this.trackingSessionPort = trackingSessionPort;
    }

    public void execute(Long driverId) {
        if (!trackingSessionPort.isTracking(driverId)) {
            throw new GeneralException(404, "Driver " + driverId + " is not being tracked");
        }
        trackingSessionPort.endTrackingSession(driverId);
    }
}