package com.gtu.driver_tracker.application.usecase;

import java.time.Instant;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gtu.driver_tracker.domain.exception.GeneralException;
import com.gtu.driver_tracker.domain.model.TrackingSession;
import com.gtu.driver_tracker.domain.repository.TrackingSessionRepository;
import com.gtu.driver_tracker.domain.service.DriverIdentityPort;
import com.gtu.driver_tracker.infrastructure.logs.LogPublisher;

@Component
public class StartTrackingUseCase {
    private final TrackingSessionRepository trackingSessionPort;
    private final DriverIdentityPort driverVerificationPort;
    private final LogPublisher logPublisher;

    public StartTrackingUseCase(TrackingSessionRepository trackingSessionPort, DriverIdentityPort driverVerificationPort,
                                LogPublisher logPublisher) {
        this.trackingSessionPort = trackingSessionPort;
        this.driverVerificationPort = driverVerificationPort;
        this.logPublisher = logPublisher;
    }

    public TrackingSession execute(Long driverId) {
        var driver = driverVerificationPort.getDriverById(driverId);
        if (driver == null) {
            throw new GeneralException(404, "Driver "+ driverId + " not found");
        }
        if (trackingSessionPort.isTracking(driverId)) {
            logPublisher.sendLog(
                Instant.now().toString(),
                "driver-tracker-service",
                "WARN",
                "Attempt to start tracking an already tracked driver",
                Map.of("driverId", driverId)
            );
            throw new GeneralException(409, "Driver "+ driverId + " is already being tracked");
        }
        var trackingSession = new TrackingSession(driver);
        trackingSessionPort.startTrackingSession(trackingSession);
        logPublisher.sendLog(
            Instant.now().toString(),
            "driver-tracker-service",
            "INFO",
            "Started tracking session for driver",
            Map.of("driverId", driverId, "sessionId", trackingSession.getSessionId())
        );
        return trackingSession;
    }
}
