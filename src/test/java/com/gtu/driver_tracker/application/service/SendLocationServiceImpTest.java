package com.gtu.driver_tracker.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtu.driver_tracker.application.dto.DriverLocationDTO;
import com.gtu.driver_tracker.config.RabbitMQConfig;
import com.gtu.driver_tracker.domain.exception.GeneralException;
import com.gtu.driver_tracker.domain.model.Location;
import com.gtu.driver_tracker.domain.model.TrackingSession;
import com.gtu.driver_tracker.domain.repository.DriverLocationRepository;
import com.gtu.driver_tracker.domain.repository.TrackingSessionRepository;
import com.gtu.driver_tracker.infrastructure.messaging.event.LocationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.time.Instant;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;





class SendLocationServiceImpTest {

    private RabbitTemplate rabbitTemplate;
    private TrackingSessionRepository sessionPort;
    private DriverLocationRepository driverLocationRepository;
    private SimpMessagingTemplate messagingTemplate;
    private SendLocationServiceImp service;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        sessionPort = mock(TrackingSessionRepository.class);
        driverLocationRepository = mock(DriverLocationRepository.class);
        messagingTemplate = mock(SimpMessagingTemplate.class);
        service = new SendLocationServiceImp(rabbitTemplate, sessionPort, driverLocationRepository, messagingTemplate);
    }

    @Test
    void updateLocation_shouldSendLocationEventToRabbitMQ() throws Exception {
        Long driverId = 1L;
        double latitude = 10.0;
        double longitude = 20.0;

        service.updateLocation(driverId, latitude, longitude);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(rabbitTemplate).convertAndSend(eq(RabbitMQConfig.DRIVER_EXCHANGE), eq(RabbitMQConfig.DRIVER_ROUTING_KEY), captor.capture());

        String sentMessage = captor.getValue();
        ObjectMapper mapper = new ObjectMapper();
        LocationEvent event = mapper.readValue(sentMessage, LocationEvent.class);

        assertEquals(driverId, event.getDriverId());
        assertEquals(latitude, event.getLatitude());
        assertEquals(longitude, event.getLongitude());
    }

    @Test
    void notifyDriverLocationChange_shouldSendDriverLocationDTO_whenSessionIsValid() {
        Long driverId = 1L;
        UUID sessionId = UUID.randomUUID();
        Location location = new Location(10.0, 20.0, Instant.now());

        var session = mockSession(driverId, sessionId, "John Doe");
        when(sessionPort.getTrackingSessionById(driverId)).thenReturn((TrackingSession) session);
        when(sessionPort.isTracking(driverId)).thenReturn(true);
        when(driverLocationRepository.getLocationByDriverId(driverId)).thenReturn(null);

        service.notifyDriverLocationChange(driverId, sessionId, location);

        ArgumentCaptor<DriverLocationDTO> captor = ArgumentCaptor.forClass(DriverLocationDTO.class);
        verify(messagingTemplate).convertAndSend(eq("/topic/tracking/drivers"), captor.capture());
        DriverLocationDTO dto = captor.getValue();

        assertEquals(driverId, dto.getDriverId());
        assertEquals("John Doe", dto.getDriverName());
        assertEquals(location, dto.getLocation());
        assertEquals(0, dto.getSpeed());
        verify(driverLocationRepository).saveLocation(driverId, location);
    }

    @Test
    void notifyDriverLocationChange_shouldThrowException_whenSessionIsInvalid() {
        Long driverId = 1L;
        UUID sessionId = UUID.randomUUID();
        Location location = new Location(10.0, 20.0, Instant.now());

        when(sessionPort.getTrackingSessionById(driverId)).thenReturn(null);

        GeneralException ex = assertThrows(GeneralException.class, () ->
                service.notifyDriverLocationChange(driverId, sessionId, location)
        );
        assertEquals(401, ex.getStatusCode());
    }

    @Test
    void notifyDriverLocationChange_shouldCalculateSpeed_whenLastLocationExists() {
        Long driverId = 1L;
        UUID sessionId = UUID.randomUUID();
        Location lastLocation = new Location(10.0, 20.0, Instant.now().minusSeconds(60));
        Location newLocation = new Location(10.1, 20.1, Instant.now());

        var session = mockSession(driverId, sessionId, "Jane Doe");
        when(sessionPort.getTrackingSessionById(driverId)).thenReturn((TrackingSession) session);
        when(sessionPort.isTracking(driverId)).thenReturn(true);
        when(driverLocationRepository.getLocationByDriverId(driverId)).thenReturn(lastLocation);
        when(driverLocationRepository.getLastUpdateTimeByDriverId(driverId)).thenReturn(lastLocation.getTimestamp());

        service.notifyDriverLocationChange(driverId, sessionId, newLocation);

        verify(driverLocationRepository).saveLocation(driverId, newLocation);
        verify(messagingTemplate).convertAndSend(eq("/topic/tracking/drivers"), any(DriverLocationDTO.class));
    }

    @Test
    void notifyDriverError_shouldSendErrorToCorrectTopic() {
        Long driverId = 1L;
        GeneralException exception = new GeneralException(500, "Test error");

        service.notifyDriverError(driverId, exception);

        verify(messagingTemplate).convertAndSend("/topic/errors/" + driverId, exception);
    }

    // Helper to mock a session object
    private Object mockSession(Long driverId, UUID sessionId, String driverName) {
        var session = mock(com.gtu.driver_tracker.domain.model.TrackingSession.class);
        when(session.getSessionId()).thenReturn(sessionId);
        when(session.getDriverId()).thenReturn(driverId);
        when(session.getDriverName()).thenReturn(driverName);
        return session;
    }
}