package com.gtu.driver_tracker.infrastructure.dto;

public record ApiResponse<T>(String message, T data, int status) {
}
