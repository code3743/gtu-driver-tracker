package com.gtu.driver_tracker.application.usecase;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;
import com.gtu.driver_tracker.application.dto.LocationMessageDTO;
import com.gtu.driver_tracker.application.mapper.LocationMapper;
import com.gtu.driver_tracker.domain.exception.GeneralException;
import com.gtu.driver_tracker.domain.service.LocationService;
import com.gtu.driver_tracker.infrastructure.logs.LogPublisher;

@Component
public class SendDriverLocationUseCase {

    private final LocationService locationService;
    private final LogPublisher logPublisher;

    public SendDriverLocationUseCase(LocationService locationService, LogPublisher logPublisher) {
        this.locationService = locationService;
        this.logPublisher = logPublisher;
    }

    public void execute(Long driverId, String sessionId, LocationMessageDTO dto) {
        try {
            locationService.notifyDriverLocationChange(driverId, UUID.fromString(sessionId), LocationMapper.toDomain(dto));
            locationService.updateLocation(driverId, dto.getLatitude(), dto.getLongitude());
        } catch (IllegalArgumentException  e) {
            locationService.notifyDriverError(driverId, new GeneralException(400, "Invalid input: " + e.getMessage()));
        } catch (GeneralException e) {
            locationService.notifyDriverError(driverId, e);
        } catch (Exception e) {
            logPublisher.sendLog(
                Instant.now().toString(),
                "driver-tracker-service",
                "ERROR",
                "Error sending driver location",
                Map.of("driverId", driverId, "sessionId", sessionId, "error", e.getMessage(), "class", e.getClass().getSimpleName(), "method", "SendDriverLocationUseCase.execute")
            );
            e.printStackTrace();
        }
    }

}
