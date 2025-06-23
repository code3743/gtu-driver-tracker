package com.gtu.driver_tracker.application.usecase;

import com.gtu.driver_tracker.application.dto.LocationMessageDTO;
import com.gtu.driver_tracker.domain.exception.GeneralException;
import com.gtu.driver_tracker.domain.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;





class SendDriverLocationUseCaseTest {

    private LocationService locationService;
    private SendDriverLocationUseCase useCase;

    @BeforeEach
    void setUp() {
        locationService = mock(LocationService.class);
        useCase = new SendDriverLocationUseCase(locationService);
    }

    @Test
    void execute_shouldNotifyAndUpdateLocation_whenInputIsValid() {
        Long driverId = 1L;
        String sessionId = UUID.randomUUID().toString();
        LocationMessageDTO dto = new LocationMessageDTO();
        dto.setLatitude(10.0);
        dto.setLongitude(20.0);

        useCase.execute(driverId, sessionId, dto);

        verify(locationService).notifyDriverLocationChange(eq(driverId), eq(UUID.fromString(sessionId)), any());
        verify(locationService).updateLocation(driverId, dto.getLatitude(), dto.getLongitude());
        verify(locationService, never()).notifyDriverError(anyLong(), any());
    }

    @Test
    void execute_shouldNotifyDriverError_whenSessionIdIsInvalid() {
        Long driverId = 2L;
        String invalidSessionId = "not-a-uuid";
        LocationMessageDTO dto = new LocationMessageDTO();

        useCase.execute(driverId, invalidSessionId, dto);

        ArgumentCaptor<GeneralException> captor = ArgumentCaptor.forClass(GeneralException.class);
        verify(locationService).notifyDriverError(eq(driverId), captor.capture());
        assertEquals(400, captor.getValue().getStatusCode());
        assertTrue(captor.getValue().getMessage().contains("Invalid input"));
    }

    @Test
    void execute_shouldNotifyDriverError_whenGeneralExceptionIsThrown() {
        Long driverId = 3L;
        String sessionId = UUID.randomUUID().toString();
        LocationMessageDTO dto = new LocationMessageDTO();

        doThrow(new GeneralException(500, "Service error"))
                .when(locationService).notifyDriverLocationChange(anyLong(), any(UUID.class), any());

        useCase.execute(driverId, sessionId, dto);

        verify(locationService).notifyDriverError(eq(driverId), any(GeneralException.class));
    }

    @Test
    void execute_shouldPrintStackTrace_whenUnexpectedExceptionIsThrown() {
        Long driverId = 4L;
        String sessionId = UUID.randomUUID().toString();
        LocationMessageDTO dto = new LocationMessageDTO();

        doThrow(new RuntimeException("Unexpected")).when(locationService)
                .notifyDriverLocationChange(anyLong(), any(UUID.class), any());

        // No exception should be thrown out of execute
        assertDoesNotThrow(() -> useCase.execute(driverId, sessionId, dto));
        verify(locationService, never()).notifyDriverError(eq(driverId), any(GeneralException.class));
    }
}