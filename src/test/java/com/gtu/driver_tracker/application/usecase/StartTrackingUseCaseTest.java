package com.gtu.driver_tracker.application.usecase;

import com.gtu.driver_tracker.domain.exception.GeneralException;
import com.gtu.driver_tracker.domain.model.Driver;
import com.gtu.driver_tracker.domain.model.TrackingSession;
import com.gtu.driver_tracker.domain.repository.TrackingSessionRepository;
import com.gtu.driver_tracker.domain.service.DriverIdentityPort;
import com.gtu.driver_tracker.infrastructure.logs.LogPublisher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Map;

class StartTrackingUseCaseTest {

    private TrackingSessionRepository trackingSessionPort;
    private DriverIdentityPort driverIdentityPort;
    private StartTrackingUseCase useCase;
    private LogPublisher logPublisher;  

    @BeforeEach
    void setUp() {
        trackingSessionPort = mock(TrackingSessionRepository.class);
        driverIdentityPort = mock(DriverIdentityPort.class);
        logPublisher = mock(LogPublisher.class);
        useCase = new StartTrackingUseCase(trackingSessionPort, driverIdentityPort, logPublisher);
    }

    @Test
    void execute_shouldStartTrackingSession_whenDriverExistsAndNotTracked() {
        Long driverId = 1L;
        Driver driver = new Driver(driverId, "Test Driver");

        when(driverIdentityPort.getDriverById(driverId)).thenReturn(driver);
        when(trackingSessionPort.isTracking(driverId)).thenReturn(false);

        TrackingSession session = useCase.execute(driverId);

        assertNotNull(session);
        assertEquals(driverId, session.getDriverId());
        verify(trackingSessionPort).startTrackingSession(session);
        verify(logPublisher).sendLog(
            anyString(), eq("driver-tracker-service"), eq("INFO"),
            eq("Started tracking session for driver"),
            argThat(map -> map.get("driverId").equals(driverId) && map.get("sessionId") != null)
        );
    }

    @Test
    void execute_shouldThrowException_whenDriverNotFound() {
        Long driverId = 2L;
        when(driverIdentityPort.getDriverById(driverId)).thenReturn(null);

        GeneralException exception = assertThrows(GeneralException.class, () -> useCase.execute(driverId));
        assertEquals(404, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("not found"));
        verify(logPublisher, never()).sendLog(any(), any(), any(), any(), any());
    }

    @Test
    void execute_shouldThrowException_whenDriverIsAlreadyBeingTracked() {
        Long driverId = 3L;
        Driver driver = new Driver(driverId, "Existing Driver");

        when(driverIdentityPort.getDriverById(driverId)).thenReturn(driver);
        when(trackingSessionPort.isTracking(driverId)).thenReturn(true);

        GeneralException exception = assertThrows(GeneralException.class, () -> useCase.execute(driverId));
        assertEquals(409, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("already being tracked"));
        verify(logPublisher).sendLog(anyString(), eq("driver-tracker-service"), eq("WARN"),
                eq("Attempt to start tracking an already tracked driver"),
                eq(Map.of("driverId", driverId)));
    }
}
