package com.gtu.driver_tracker.presentation.ws;


import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.gtu.driver_tracker.application.dto.LocationMessageDTO;
import com.gtu.driver_tracker.application.usecase.SendDriverLocationUseCase;


@Controller
public class LocationWebSocketController {

    private final SendDriverLocationUseCase sendLocationUseCase;


    public LocationWebSocketController(SendDriverLocationUseCase useCase) {
        this.sendLocationUseCase = useCase;
    }

    @MessageMapping("/tracking/driver/{driverId}/send")
    public void sendLocation(@DestinationVariable Long driverId,
                             LocationMessageDTO locationMessage,
                             @Header("token") String sessionId) {
       
      sendLocationUseCase.execute(driverId, sessionId, locationMessage);
       
    }
}
