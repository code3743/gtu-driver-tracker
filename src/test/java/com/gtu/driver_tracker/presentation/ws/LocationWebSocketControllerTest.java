
package com.gtu.driver_tracker.presentation.ws;
import com.gtu.driver_tracker.application.dto.LocationMessageDTO;
import com.gtu.driver_tracker.application.usecase.SendDriverLocationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;




class LocationWebSocketControllerTest {

    private SendDriverLocationUseCase sendDriverLocationUseCase;
    private LocationWebSocketController controller;

    @BeforeEach
    void setUp() {
        sendDriverLocationUseCase = mock(SendDriverLocationUseCase.class);
        controller = new LocationWebSocketController(sendDriverLocationUseCase);
    }

    @Test
    void sendLocation_shouldCallUseCaseWithCorrectArguments() {
        Long driverId = 42L;
        String sessionId = "test-session-id";
        LocationMessageDTO dto = new LocationMessageDTO();
        dto.setLatitude(12.34);
        dto.setLongitude(56.78);

        controller.sendLocation(driverId, dto, sessionId);

        verify(sendDriverLocationUseCase).execute(driverId, sessionId, dto);
    }

    @Test
    void sendLocation_shouldHandleNullLocationMessage() {
        Long driverId = 1L;
        String sessionId = "session";
        controller.sendLocation(driverId, null, sessionId);
        verify(sendDriverLocationUseCase).execute(driverId, sessionId, null);
    }

    @Test
    void sendLocation_shouldHandleNullSessionId() {
        Long driverId = 1L;
        LocationMessageDTO dto = new LocationMessageDTO();
        controller.sendLocation(driverId, dto, null);
        verify(sendDriverLocationUseCase).execute(driverId, null, dto);
    }
}