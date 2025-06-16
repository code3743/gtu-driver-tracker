package com.gtu.driver_tracker.application.usecase;

import org.springframework.stereotype.Component;

import com.gtu.driver_tracker.domain.exception.HttpException;
import com.gtu.driver_tracker.domain.model.TrackingSession;
import com.gtu.driver_tracker.domain.service.DriverIdentityPort;
import com.gtu.driver_tracker.domain.service.TrackingSessionPort;

@Component
public class StartTrackingUseCase {
    private final TrackingSessionPort trackingSessionPort;
    private final DriverIdentityPort driverVerificationPort;

    public StartTrackingUseCase(TrackingSessionPort trackingSessionPort, DriverIdentityPort driverVerificationPort) {
        this.trackingSessionPort = trackingSessionPort;
        this.driverVerificationPort = driverVerificationPort;
    }

    public TrackingSession execute(Long driverId) {
        var driver = driverVerificationPort.getDriverById(driverId);
        if (driver == null) {
            throw new HttpException(404, "Driver "+ driverId + " not found");
        }
        if (trackingSessionPort.isTracking(driverId)) {
            throw new HttpException(409, "Driver "+ driverId + " is already being tracked");
        }
        var trackingSession = new TrackingSession(driver);
        trackingSessionPort.startTrackingSession(trackingSession);
        return trackingSession;
    }
}
