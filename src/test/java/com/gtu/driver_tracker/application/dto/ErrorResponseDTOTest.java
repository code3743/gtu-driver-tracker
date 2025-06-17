package com.gtu.driver_tracker.application.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseDTOTest {

    @Test
    void constructorAndGetters_shouldSetAllFieldsCorrectly() {
        int status = 404;
        String error = "Not Found";
        String message = "The requested resource was not found";
        String path = "/api/drivers/123";

        ErrorResponseDTO dto = new ErrorResponseDTO(status, error, message, path);

        assertEquals(status, dto.getStatus());
        assertEquals(error, dto.getError());
        assertEquals(message, dto.getMessage());
        assertEquals(path, dto.getPath());
    }

    @Test
    void setters_shouldUpdateFieldsCorrectly() {
        ErrorResponseDTO dto = new ErrorResponseDTO(500, "Internal Server Error", "Unexpected error", "/api/test");

        dto.setStatus(400);
        dto.setError("Bad Request");
        dto.setMessage("Invalid input");
        dto.setPath("/api/input");

        assertEquals(400, dto.getStatus());
        assertEquals("Bad Request", dto.getError());
        assertEquals("Invalid input", dto.getMessage());
        assertEquals("/api/input", dto.getPath());
    }
}
