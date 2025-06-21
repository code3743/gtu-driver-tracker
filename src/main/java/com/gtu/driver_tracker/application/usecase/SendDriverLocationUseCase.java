package com.gtu.driver_tracker.application.usecase;

import java.util.UUID;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.gtu.driver_tracker.application.dto.DriverLocationDTO;
import com.gtu.driver_tracker.application.dto.LocationMessageDTO;
import com.gtu.driver_tracker.application.mapper.LocationMapper;
import com.gtu.driver_tracker.domain.repository.DriverLocationRepository;
import com.gtu.driver_tracker.domain.repository.TrackingSessionRepository;
import com.gtu.driver_tracker.utils.GpsUtils;

@Component
public class SendDriverLocationUseCase {

    private final TrackingSessionRepository sessionPort;
  
    private final DriverLocationRepository driverLocationRepository;

    public SendDriverLocationUseCase(TrackingSessionRepository sessionPort, DriverLocationRepository driverLocationRepository) {
        this.sessionPort = sessionPort;
        this.driverLocationRepository = driverLocationRepository;
    }

    public void execute(Long driverId, UUID sessionId, LocationMessageDTO dto, SimpMessagingTemplate messagingTemplate) {
      
        
        var session = sessionPort.getTrackingSessionById(driverId);
        
        if (!isValidSession(driverId, sessionId)) {
            throw new IllegalArgumentException("Invalid session for driver " + driverId);
        }

        var location = LocationMapper.toDomain(dto);
        var lastLocation = driverLocationRepository.getLocationByDriverId(driverId);
        var lastUpdateTime = driverLocationRepository.getLastUpdateTimeByDriverId(driverId);

        double speed = lastLocation != null ? GpsUtils.calculateSpeed(lastUpdateTime, lastLocation.getLatitude(), lastLocation.getLongitude(), location.getLatitude(), location.getLongitude()) : 0;
        driverLocationRepository.saveLocation(driverId, location);
        var driverLocation = new DriverLocationDTO(driverId, session.getDriverName(), location, speed);
        messagingTemplate.convertAndSend("/topic/tracking/drivers", driverLocation);
    }

    private boolean isValidSession(Long driverId, UUID sessionId) {
        var session = sessionPort.getTrackingSessionById(driverId);
        return session != null && session.getSessionId().equals(sessionId) && sessionPort.isTracking(driverId) && session.getDriverId().equals(driverId);
    }
}
