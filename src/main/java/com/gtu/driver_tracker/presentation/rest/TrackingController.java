package com.gtu.driver_tracker.presentation.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtu.driver_tracker.application.dto.ResponseDTO;
import com.gtu.driver_tracker.application.usecase.StartTrackingUseCase;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/tracking")
@Tag(name = "Tracking", description = "Endpoints for managing tracking sessions")
@CrossOrigin(origins = "*")
public class TrackingController {

    final StartTrackingUseCase startTrackingUseCase;
    public TrackingController(StartTrackingUseCase startTrackingUseCase) {
        this.startTrackingUseCase = startTrackingUseCase;
    }


    @GetMapping("/start/${id}")
    public ResponseDTO<String> startTracking(@RequestParam Long id) {
        startTrackingUseCase.execute(id);
        return new ResponseDTO<>("Tracking started for driver ID: " + id, null, 200);
    }
       
}
