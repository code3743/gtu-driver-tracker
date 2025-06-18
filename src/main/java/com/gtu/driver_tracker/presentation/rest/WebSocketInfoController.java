package com.gtu.driver_tracker.presentation.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/ws")
@Tag(name = "WebSocket", description = "WebSocket Integration Info")
public class WebSocketInfoController {

    @GetMapping("/info")
    @Operation(summary = "WebSocket Connection Info", description = """
        ⚙️ **Conexión WebSocket con STOMP:**

        **1. URL de conexión WebSocket:**  
        `ws://<host>:<puerto>/ws/tracking`  
        Ejemplo: `ws://localhost:8080/ws/tracking`

        **2. Protocolo:**  
        STOMP sobre WebSocket

        **3. Envío de ubicación (desde conductor):**  
        - Destino STOMP: `/app/tracking/driver/{driverId}/send`
        - Header requerido:
          - `token`: UUID del `sessionId` generado al iniciar sesión de rastreo (`/tracking/start/{driverId}`)

        **4. Suscripción (cliente/pasajero):**  
        - Destino STOMP: `/topic/tracking/drivers`
        - Recibe actualizaciones en tiempo real de todos los conductores activos.

        ** Ejemplo de mensaje enviado por conductor:**
        ```json
        {
          "latitude": "10.123456",
          "longitude": "-75.123456"
        }
        ```

        ** Ejemplo de mensaje recibido por cliente:**
        ```json
        {
          "driverId": 17,
          "driverName": "Bus 17",
          "location": {
            "latitude": "10.123456",
            "longitude": "-75.123456",
            "timestamp": "2025-06-18T12:34:56.789Z"
          }
        }
        ```

        **Nota:**  
        El `token` es validado internamente para garantizar que el conductor esté autenticado y en sesión activa. Si es inválido o ha expirado, el mensaje será ignorado.

       """)
    public ResponseEntity<Void> getWebSocketInfo() {
        return ResponseEntity.ok().build();
    }
}
