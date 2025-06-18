package com.gtu.driver_tracker.application.usecase;

import java.util.UUID;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.gtu.driver_tracker.application.dto.DriverLocationDTO;
import com.gtu.driver_tracker.application.dto.LocationMessageDTO;
import com.gtu.driver_tracker.application.mapper.LocationMapper;
import com.gtu.driver_tracker.domain.service.TrackingSessionPort;

@Component
public class SendDriverLocationUseCase {

    private final TrackingSessionPort sessionPort;
    private final SimpMessagingTemplate messagingTemplate;

    public SendDriverLocationUseCase(TrackingSessionPort sessionPort, SimpMessagingTemplate messagingTemplate) {
        this.sessionPort = sessionPort;
        this.messagingTemplate = messagingTemplate;
    }

    public void execute(Long driverId, UUID sessionId, LocationMessageDTO dto) {
        var session = sessionPort.getTrackingSessionById(driverId);
        
        if (!isValidSession(driverId, sessionId)) {
            return;
        }

        var location = LocationMapper.toDomain(dto);
        var driverLocation = new DriverLocationDTO(driverId, session.getDriverName(), location);
        messagingTemplate.convertAndSend("/topic/tracking/drivers", driverLocation);
    }

    private boolean isValidSession(Long driverId, UUID sessionId) {
        var session = sessionPort.getTrackingSessionById(driverId);
        return session != null && session.getSessionId().equals(sessionId) && sessionPort.isTracking(driverId) && session.getDriverId().equals(driverId);
    }
}
