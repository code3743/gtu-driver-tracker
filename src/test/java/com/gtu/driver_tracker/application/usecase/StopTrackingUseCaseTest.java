package com.gtu.driver_tracker.application.usecase;

import com.gtu.driver_tracker.domain.exception.GeneralException;
import com.gtu.driver_tracker.domain.repository.TrackingSessionRepository;
import com.gtu.driver_tracker.infrastructure.logs.LogPublisher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Map;

class StopTrackingUseCaseTest {

    private TrackingSessionRepository trackingSessionPort;
    private StopTrackingUseCase useCase;
    private LogPublisher logPublisher;

    @BeforeEach
    void setUp() {
        trackingSessionPort = mock(TrackingSessionRepository.class);
        logPublisher = mock(LogPublisher.class);
        useCase = new StopTrackingUseCase(trackingSessionPort, logPublisher);
    }

    @Test
    void execute_shouldEndTrackingSession_whenDriverIsBeingTracked() {
        Long driverId = 1L;
        when(trackingSessionPort.isTracking(driverId)).thenReturn(true);

        useCase.execute(driverId);

        verify(trackingSessionPort).endTrackingSession(driverId);
        verify(logPublisher).sendLog(anyString(), eq("driver-tracker-service"), eq("INFO"),
                eq("Stopped tracking session for driver"),
                eq(Map.of("driverId", driverId)));
    }

    @Test
    void execute_shouldThrowException_whenDriverIsNotBeingTracked() {
        Long driverId = 2L;
        when(trackingSessionPort.isTracking(driverId)).thenReturn(false);

        GeneralException exception = assertThrows(GeneralException.class, () -> useCase.execute(driverId));
        assertEquals(404, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("not being tracked"));
        verify(logPublisher, never()).sendLog(any(), any(), any(), any(), any());
    }
}
