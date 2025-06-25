package com.gtu.driver_tracker.application.usecase;

import java.time.Instant;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gtu.driver_tracker.domain.exception.GeneralException;
import com.gtu.driver_tracker.domain.repository.TrackingSessionRepository;
import com.gtu.driver_tracker.infrastructure.logs.LogPublisher;

@Component
public class StopTrackingUseCase {
    private final TrackingSessionRepository trackingSessionPort;
    private final LogPublisher logPublisher;

    public StopTrackingUseCase(TrackingSessionRepository trackingSessionPort,
                               LogPublisher logPublisher) {
        this.trackingSessionPort = trackingSessionPort;
        this.logPublisher = logPublisher;
    }

    public void execute(Long driverId) {
        if (!trackingSessionPort.isTracking(driverId)) {
            throw new GeneralException(404, "Driver " + driverId + " is not being tracked");
        }
        trackingSessionPort.endTrackingSession(driverId);
        logPublisher.sendLog(
            Instant.now().toString(),
            "driver-tracker-service",
            "INFO",
            "Stopped tracking session for driver",
            Map.of("driverId", driverId)
        );
    }
}