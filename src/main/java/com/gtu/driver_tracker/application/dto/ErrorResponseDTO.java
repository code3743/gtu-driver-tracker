package com.gtu.driver_tracker.application.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private int status;
    private String error;
    private Object message;
    private String path;
}