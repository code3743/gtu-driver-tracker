package com.gtu.driver_tracker.application.usecase;

import com.gtu.driver_tracker.domain.service.DriverVerificationPort;
import com.gtu.driver_tracker.domain.service.TrackingSessionPort;

public class StartTrackingUseCase {
    private final TrackingSessionPort trackingSessionPort;
    private final DriverVerificationPort driverVerificationPort;

    public StartTrackingUseCase(TrackingSessionPort trackingSessionPort, DriverVerificationPort driverVerificationPort) {
        this.trackingSessionPort = trackingSessionPort;
        this.driverVerificationPort = driverVerificationPort;
    }

    public void execute(Long driverId) {
        if (!driverVerificationPort.verifyDriver(driverId)) {
            throw new IllegalArgumentException("Invalid driver ID");
        }
        if (trackingSessionPort.isTracking(driverId)) {
            throw new IllegalStateException("Driver is already being tracked");
        }
        trackingSessionPort.startTrackingSession(driverId);
    }
}
