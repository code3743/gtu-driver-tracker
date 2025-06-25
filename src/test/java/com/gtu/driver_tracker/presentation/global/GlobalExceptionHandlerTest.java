package com.gtu.driver_tracker.presentation.global;

import com.gtu.driver_tracker.application.dto.ErrorResponseDTO;
import com.gtu.driver_tracker.domain.exception.GeneralException;
import com.gtu.driver_tracker.infrastructure.logs.LogPublisher;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.Collections;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;
    private LogPublisher logPublisher;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        logPublisher = mock(LogPublisher.class);
        handler.logPublisher = logPublisher;
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test-uri");
    }

    @Test
    void handleIllegalArgumentException_shouldReturnBadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");

        ResponseEntity<ErrorResponseDTO> response = handler.handleIllegalArgumentException(ex, request);

        assertEquals(400, response.getStatusCode().value());
        ErrorResponseDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals("Bad Request", body.getError());
        assertEquals("Invalid argument", body.getMessage());
        assertEquals("/test-uri", body.getPath());
    }

    @Test
    void handleHttpException_shouldReturnCustomStatus() {
        GeneralException ex = new GeneralException(404, "Not found");

        ResponseEntity<ErrorResponseDTO> response = handler.handleHttpException(ex, request);

        assertEquals(404, response.getStatusCode().value());
        ErrorResponseDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.getStatus());
        assertEquals("Not Found", body.getError());
        assertEquals("Not found", body.getMessage());
        assertEquals("/test-uri", body.getPath());
    }

    @Test
    void handleValidationExceptions_shouldReturnFieldErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field1", "must not be null");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponseDTO> response = handler.handleValidationExceptions(ex, request);

        assertEquals(400, response.getStatusCode().value());
        ErrorResponseDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals("Validation Failed", body.getError());
        assertTrue(body.getMessage() instanceof Map);
        Map<?, ?> errors = (Map<?, ?>) body.getMessage();
        assertEquals("must not be null", errors.get("field1"));
        assertEquals("/test-uri", body.getPath());
    }

    @Test
    void handleUnexpectedException_shouldLogAndReturnInternalServerError() {
        Exception ex = new Exception("Unexpected error");

        ResponseEntity<ErrorResponseDTO> response = handler.handleUnexpectedException(ex, request);

        assertEquals(500, response.getStatusCode().value());
        verify(logPublisher).sendLog(anyString(), eq("driver-tracker-service"), eq("ERROR"),
                eq("Unexpected error occurred"), anyMap());
    }
}