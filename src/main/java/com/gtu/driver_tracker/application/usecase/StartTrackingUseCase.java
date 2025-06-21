package com.gtu.driver_tracker.application.usecase;

import org.springframework.stereotype.Component;

import com.gtu.driver_tracker.domain.exception.GeneralException;
import com.gtu.driver_tracker.domain.model.TrackingSession;
import com.gtu.driver_tracker.domain.repository.TrackingSessionRepository;
import com.gtu.driver_tracker.domain.service.DriverIdentityPort;

@Component
public class StartTrackingUseCase {
    private final TrackingSessionRepository trackingSessionPort;
    private final DriverIdentityPort driverVerificationPort;

    public StartTrackingUseCase(TrackingSessionRepository trackingSessionPort, DriverIdentityPort driverVerificationPort) {
        this.trackingSessionPort = trackingSessionPort;
        this.driverVerificationPort = driverVerificationPort;
    }

    public TrackingSession execute(Long driverId) {
        var driver = driverVerificationPort.getDriverById(driverId);
        if (driver == null) {
            throw new GeneralException(404, "Driver "+ driverId + " not found");
        }
        if (trackingSessionPort.isTracking(driverId)) {
            throw new GeneralException(409, "Driver "+ driverId + " is already being tracked");
        }
        var trackingSession = new TrackingSession(driver);
        trackingSessionPort.startTrackingSession(trackingSession);
        return trackingSession;
    }
}
