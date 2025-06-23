package com.gtu.driver_tracker.infrastructure.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor 
@Getter
public class LocationEvent {
    private Long driverId;
    private double latitude;
    private double longitude;
}
