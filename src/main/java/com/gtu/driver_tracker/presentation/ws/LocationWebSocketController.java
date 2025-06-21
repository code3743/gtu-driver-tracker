package com.gtu.driver_tracker.presentation.ws;

import java.util.UUID;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.gtu.driver_tracker.application.dto.LocationMessageDTO;
import com.gtu.driver_tracker.application.usecase.SendDriverLocationUseCase;
import com.gtu.driver_tracker.domain.exception.GeneralException;

@Controller
public class LocationWebSocketController {

    private final SendDriverLocationUseCase sendLocationUseCase;
    private final SimpMessagingTemplate messagingTemplate;

    public LocationWebSocketController(SendDriverLocationUseCase useCase, SimpMessagingTemplate messagingTemplate) {
        this.sendLocationUseCase = useCase;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/tracking/driver/{driverId}/send")
    public void sendLocation(@DestinationVariable Long driverId,
                             LocationMessageDTO locationMessage,
                             @Header("token") String sessionId) {
       try {
         sendLocationUseCase.execute(driverId, UUID.fromString(sessionId), locationMessage, messagingTemplate);
       } catch (GeneralException e) {
         messagingTemplate.convertAndSend(
                "/topic/errors/" + driverId,
                e
            );
       }
       catch (Exception e) {
        messagingTemplate.convertAndSend(
                "/topic/errors/" + driverId,
                new GeneralException(500, "An unexpected error occurred: " + e.getMessage())
            );
       }
    }
}
