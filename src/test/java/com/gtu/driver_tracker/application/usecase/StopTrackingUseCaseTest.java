package com.gtu.driver_tracker.application.usecase;

import com.gtu.driver_tracker.domain.exception.GeneralException;
import com.gtu.driver_tracker.domain.repository.TrackingSessionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StopTrackingUseCaseTest {

    private TrackingSessionRepository trackingSessionPort;
    private StopTrackingUseCase useCase;

    @BeforeEach
    void setUp() {
        trackingSessionPort = mock(TrackingSessionRepository.class);
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

        GeneralException exception = assertThrows(GeneralException.class, () -> useCase.execute(driverId));
        assertEquals(404, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("not being tracked"));
    }
}
