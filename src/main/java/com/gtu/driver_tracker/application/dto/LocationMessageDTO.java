package com.gtu.driver_tracker.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationMessageDTO {
    private double latitude;
    private double longitude;
}