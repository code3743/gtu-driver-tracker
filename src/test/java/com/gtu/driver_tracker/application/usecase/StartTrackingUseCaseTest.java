package com.gtu.driver_tracker.application.usecase;

import com.gtu.driver_tracker.domain.exception.HttpException;
import com.gtu.driver_tracker.domain.model.Driver;
import com.gtu.driver_tracker.domain.model.TrackingSession;
import com.gtu.driver_tracker.domain.service.DriverIdentityPort;
import com.gtu.driver_tracker.domain.service.TrackingSessionPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StartTrackingUseCaseTest {

    private TrackingSessionPort trackingSessionPort;
    private DriverIdentityPort driverIdentityPort;
    private StartTrackingUseCase useCase;

    @BeforeEach
    void setUp() {
        trackingSessionPort = mock(TrackingSessionPort.class);
        driverIdentityPort = mock(DriverIdentityPort.class);
        useCase = new StartTrackingUseCase(trackingSessionPort, driverIdentityPort);
    }

    @Test
    void execute_shouldStartTrackingSession_whenDriverExistsAndNotTracked() {
        Long driverId = 1L;
        Driver driver = new Driver(driverId, "Test Driver");

        when(driverIdentityPort.getDriverById(driverId)).thenReturn(driver);
        when(trackingSessionPort.isTracking(driverId)).thenReturn(false);

        TrackingSession session = useCase.execute(driverId);

        assertNotNull(session);
        assertEquals(driverId, session.getSessionId());
        verify(trackingSessionPort).startTrackingSession(session);
    }

    @Test
    void execute_shouldThrowException_whenDriverNotFound() {
        Long driverId = 2L;
        when(driverIdentityPort.getDriverById(driverId)).thenReturn(null);

        HttpException exception = assertThrows(HttpException.class, () -> useCase.execute(driverId));
        assertEquals(404, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void execute_shouldThrowException_whenDriverIsAlreadyBeingTracked() {
        Long driverId = 3L;
        Driver driver = new Driver(driverId, "Existing Driver");

        when(driverIdentityPort.getDriverById(driverId)).thenReturn(driver);
        when(trackingSessionPort.isTracking(driverId)).thenReturn(true);

        HttpException exception = assertThrows(HttpException.class, () -> useCase.execute(driverId));
        assertEquals(409, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("already being tracked"));
    }
}
