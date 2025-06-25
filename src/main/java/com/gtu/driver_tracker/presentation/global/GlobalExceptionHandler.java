package com.gtu.driver_tracker.presentation.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.gtu.driver_tracker.application.dto.ErrorResponseDTO;
import com.gtu.driver_tracker.domain.exception.GeneralException;
import com.gtu.driver_tracker.infrastructure.logs.LogPublisher;

import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final LogPublisher logPublisher;
    public GlobalExceptionHandler(LogPublisher logPublisher) {
        this.logPublisher = logPublisher;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        ErrorResponseDTO response = new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            ex.getMessage(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpException(GeneralException ex, HttpServletRequest request) {
        ErrorResponseDTO response = new ErrorResponseDTO(
            ex.getStatusCode(),
            HttpStatus.valueOf(ex.getStatusCode()).getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatusCode()));
    }

    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        ErrorResponseDTO response = new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            fieldErrors, 
            request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleUnexpectedException(Exception ex, HttpServletRequest request) {
        logPublisher.sendLog(
            Instant.now().toString(),
            "driver-tracker-service",
            "ERROR",
            "Unexpected error occurred",
            Map.of("uri", request.getRequestURI(), "error", ex.getMessage(),"class", ex.getClass().getName(), "method", "handleUnexpectedException")
        );
        ErrorResponseDTO response = new ErrorResponseDTO(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            ex.getMessage(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}