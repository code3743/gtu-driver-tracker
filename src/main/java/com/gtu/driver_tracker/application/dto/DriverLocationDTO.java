package com.gtu.driver_tracker.application.dto;

import com.gtu.driver_tracker.domain.model.Location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverLocationDTO {
    private Long driverId;
    private String driverName;
    private Location location;
}