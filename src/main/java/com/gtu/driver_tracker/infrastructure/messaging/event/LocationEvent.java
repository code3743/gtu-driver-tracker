package com.gtu.driver_tracker.infrastructure.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LocationEvent {
    private Long driverId;
    private double latitude;
    private double longitude;
}
