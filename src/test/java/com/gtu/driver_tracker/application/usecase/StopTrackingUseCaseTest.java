package com.gtu.driver_tracker.application.usecase;

import com.gtu.driver_tracker.domain.exception.HttpException;
import com.gtu.driver_tracker.domain.service.TrackingSessionPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StopTrackingUseCaseTest {

    private TrackingSessionPort trackingSessionPort;
    private StopTrackingUseCase useCase;

    @BeforeEach
    void setUp() {
        trackingSessionPort = mock(TrackingSessionPort.class);
        useCase = new StopTrackingUseCase(trackingSessionPort);
    }

    @Test
    void execute_shouldEndTrackingSession_whenDriverIsBeingTracked() {
        Long driverId = 1L;
        when(trackingSessionPort.isTracking(driverId)).thenReturn(true);

        useCase.execute(driverId);

        verify(trackingSessionPort).endTrackingSession(driverId);
    }

    @Test
    void execute_shouldThrowException_whenDriverIsNotBeingTracked() {
        Long driverId = 2L;
        when(trackingSessionPort.isTracking(driverId)).thenReturn(false);

        HttpException exception = assertThrows(HttpException.class, () -> useCase.execute(driverId));
        assertEquals(404, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("not being tracked"));
    }
}
