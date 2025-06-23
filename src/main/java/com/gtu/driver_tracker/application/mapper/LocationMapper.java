package com.gtu.driver_tracker.application.mapper;

import com.gtu.driver_tracker.application.dto.LocationMessageDTO;
import com.gtu.driver_tracker.domain.model.Location;
import java.time.Instant;

public class LocationMapper {
   
    private LocationMapper() {}

    public static Location toDomain(LocationMessageDTO dto) {
        return new Location(dto.getLatitude(), dto.getLongitude(), Instant.now());
    }

    public static LocationMessageDTO toDTO(Location location) {
        LocationMessageDTO dto = new LocationMessageDTO();
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        return dto;
    }
}
