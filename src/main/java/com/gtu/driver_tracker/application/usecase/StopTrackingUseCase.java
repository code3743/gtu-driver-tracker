package com.gtu.driver_tracker.application.usecase;

import org.springframework.stereotype.Component;

import com.gtu.driver_tracker.domain.exception.HttpException;
import com.gtu.driver_tracker.domain.service.TrackingSessionPort;

@Component
public class StopTrackingUseCase {
    private final TrackingSessionPort trackingSessionPort;

    public StopTrackingUseCase(TrackingSessionPort trackingSessionPort) {
        this.trackingSessionPort = trackingSessionPort;
    }

    public void execute(Long driverId) {
        if (!trackingSessionPort.isTracking(driverId)) {
            throw new HttpException(404, "Driver " + driverId + " is not being tracked");
        }
        trackingSessionPort.endTrackingSession(driverId);
    }
}