package com.gtu.driver_tracker.presentation.rest;

import com.gtu.driver_tracker.application.dto.ResponseDTO;
import com.gtu.driver_tracker.application.usecase.StartTrackingUseCase;
import com.gtu.driver_tracker.application.usecase.StopTrackingUseCase;
import com.gtu.driver_tracker.domain.model.Driver;
import com.gtu.driver_tracker.domain.model.TrackingSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




class TrackingControllerTest {

    private StartTrackingUseCase startTrackingUseCase;
    private StopTrackingUseCase stopTrackingUseCase;
    private TrackingController trackingController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        startTrackingUseCase = mock(StartTrackingUseCase.class);
        stopTrackingUseCase = mock(StopTrackingUseCase.class);
        trackingController = new TrackingController(startTrackingUseCase, stopTrackingUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(trackingController).build();
    }

    @Test
    void startTracking_shouldReturnResponseDTOWithTrackingSession() throws Exception {
        Long driverId = 1L;
        TrackingSession session = new TrackingSession(new Driver(driverId, null));
        when(startTrackingUseCase.execute(driverId)).thenReturn(session);

        mockMvc.perform(get("/tracking/start/{id}", driverId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tracking started for driver ID: " + driverId))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.status").value(200));

        verify(startTrackingUseCase).execute(driverId);
    }

    @Test
    void stopTracking_shouldReturnResponseDTOWithNullData() throws Exception {
        Long driverId = 2L;

        mockMvc.perform(get("/tracking/stop/{id}", driverId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tracking stopped for driver ID: " + driverId))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.status").value(200));

        verify(stopTrackingUseCase).execute(driverId);
    }

    @Test
    void startTracking_shouldCallStartTrackingUseCaseWithCorrectId() {
        Long driverId = 3L;
        TrackingSession session = new TrackingSession(new Driver(driverId, null));
        when(startTrackingUseCase.execute(driverId)).thenReturn(session);

        ResponseDTO<TrackingSession> response = trackingController.startTracking(driverId);

        assertThat(response.getMessage()).isEqualTo("Tracking started for driver ID: " + driverId);
        assertThat(response.getData()).isEqualTo(session);
        assertThat(response.getStatus()).isEqualTo(200);
        verify(startTrackingUseCase).execute(driverId);
    }

    @Test
    void stopTracking_shouldCallStopTrackingUseCaseWithCorrectId() {
        Long driverId = 4L;

        ResponseDTO<Void> response = trackingController.stopTracking(driverId);

        assertThat(response.getMessage()).isEqualTo("Tracking stopped for driver ID: " + driverId);
        assertThat(response.getData()).isNull();
        assertThat(response.getStatus()).isEqualTo(200);
        verify(stopTrackingUseCase).execute(driverId);
    }
}