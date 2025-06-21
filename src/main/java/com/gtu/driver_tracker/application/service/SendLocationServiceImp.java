package com.gtu.driver_tracker.application.service;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtu.driver_tracker.application.dto.DriverLocationDTO;
import com.gtu.driver_tracker.config.RabbitMQConfig;
import com.gtu.driver_tracker.domain.exception.GeneralException;
import com.gtu.driver_tracker.domain.model.Location;
import com.gtu.driver_tracker.domain.repository.DriverLocationRepository;
import com.gtu.driver_tracker.domain.repository.TrackingSessionRepository;
import com.gtu.driver_tracker.domain.service.LocationService;
import com.gtu.driver_tracker.infrastructure.messaging.event.LocationEvent;
import com.gtu.driver_tracker.utils.GpsUtils;


@Service
public class SendLocationServiceImp implements LocationService {

    private final RabbitTemplate rabbitTemplate;
    private final TrackingSessionRepository sessionPort;
    private final DriverLocationRepository driverLocationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public SendLocationServiceImp(RabbitTemplate rabbitTemplate,
                                  TrackingSessionRepository sessionPort,
                                  DriverLocationRepository driverLocationRepository,
                                  SimpMessagingTemplate messagingTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.sessionPort = sessionPort;
        this.driverLocationRepository = driverLocationRepository;
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = new ObjectMapper();
    }


 
    
    private boolean isValidSession(Long driverId, UUID sessionId) {
        var session = sessionPort.getTrackingSessionById(driverId);
        return session != null && session.getSessionId().equals(sessionId) && sessionPort.isTracking(driverId) && session.getDriverId().equals(driverId);
    }




    @Override
    public void updateLocation(Long driverId, double latitude, double longitude) {
       try {
            var locationEvent = new LocationEvent(driverId, latitude, longitude);
            String message = objectMapper.writeValueAsString(locationEvent);
            rabbitTemplate.convertAndSend(RabbitMQConfig.DRIVER_EXCHANGE, RabbitMQConfig.DRIVER_ROUTING_KEY, message);
       } catch (Exception e) {
            e.printStackTrace();
       }
    }




    @Override
    public void notifyDriverLocationChange(Long driverId, UUID sessionId, Location location) {
         var session = sessionPort.getTrackingSessionById(driverId);
        
        if (!isValidSession(driverId, sessionId)) {
            throw new GeneralException(401, "Unauthorized session or driver ID mismatch");
        }

        var lastLocation = driverLocationRepository.getLocationByDriverId(driverId);
        var lastUpdateTime = driverLocationRepository.getLastUpdateTimeByDriverId(driverId);
         double speed = lastLocation != null ? GpsUtils.calculateSpeed(lastUpdateTime, lastLocation.getLatitude(), lastLocation.getLongitude(), location.getLatitude(), location.getLongitude()) : 0;
        driverLocationRepository.saveLocation(driverId, location);
        var driverLocation = new DriverLocationDTO(driverId, session.getDriverName(), location, speed);
        messagingTemplate.convertAndSend("/topic/tracking/drivers", driverLocation);
    }




    @Override
    public void notifyDriverError(Long driverId, GeneralException exception) {
       messagingTemplate.convertAndSend(
                "/topic/errors/" + driverId,
                exception
            );
    }
}
