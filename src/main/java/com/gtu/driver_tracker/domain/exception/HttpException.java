package com.gtu.driver_tracker.domain.exception;

public class HttpException extends RuntimeException {
    private final int statusCode;
    private final String message;

    public HttpException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
    
}
