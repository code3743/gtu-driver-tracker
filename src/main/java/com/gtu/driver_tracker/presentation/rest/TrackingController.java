package com.gtu.driver_tracker.presentation.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtu.driver_tracker.application.dto.ResponseDTO;
import com.gtu.driver_tracker.application.usecase.StartTrackingUseCase;
import com.gtu.driver_tracker.application.usecase.StopTrackingUseCase;
import com.gtu.driver_tracker.domain.model.TrackingSession;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/tracking")
@Tag(name = "Tracking", description = "Endpoints for managing tracking sessions")
public class TrackingController {

    final StartTrackingUseCase startTrackingUseCase;
    final StopTrackingUseCase stopTrackingUseCase;

    public TrackingController(StartTrackingUseCase startTrackingUseCase, StopTrackingUseCase stopTrackingUseCase) {
        this.startTrackingUseCase = startTrackingUseCase;
        this.stopTrackingUseCase = stopTrackingUseCase;
    }


    @GetMapping("/start/{id}")
    @Operation(summary = "Start tracking a driver by ID")
    public ResponseDTO<TrackingSession> startTracking(@PathVariable("id") Long id) {
        return new ResponseDTO<>("Tracking started for driver ID: " + id, startTrackingUseCase.execute(id), 200);
    }

    @GetMapping("/stop/{id}")
    @Operation(summary = "Stop tracking a driver by ID")
    public ResponseDTO<Void> stopTracking(@PathVariable("id") Long id) {
        stopTrackingUseCase.execute(id);
        return new ResponseDTO<>("Tracking stopped for driver ID: " + id, null, 200);
    }

    

}
