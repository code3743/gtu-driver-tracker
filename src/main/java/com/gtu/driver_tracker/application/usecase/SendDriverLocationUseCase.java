package com.gtu.driver_tracker.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Component;
import com.gtu.driver_tracker.application.dto.LocationMessageDTO;
import com.gtu.driver_tracker.application.mapper.LocationMapper;
import com.gtu.driver_tracker.domain.exception.GeneralException;
import com.gtu.driver_tracker.domain.service.LocationService;

@Component
public class SendDriverLocationUseCase {

    private final LocationService locationService;

    public SendDriverLocationUseCase(LocationService locationService) {
        this.locationService = locationService;
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
            e.printStackTrace();
        }
    }

}
