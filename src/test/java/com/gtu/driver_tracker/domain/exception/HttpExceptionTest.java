package com.gtu.driver_tracker.domain.exception;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class HttpExceptionTest {

    @Test
    void constructorAndGettersShouldWork() {
        int statusCode = 404;
        String message = "Not Found";
        GeneralException exception = new GeneralException(statusCode, message);

        assertEquals(statusCode, exception.getStatusCode());
        assertEquals(message, exception.getMessage());
        assertEquals(message, exception.getMessage()); // getMessage() overridden
    }

    @Test
    void getMessageShouldReturnCustomMessage() {
        String customMessage = "Custom error message";
        GeneralException exception = new GeneralException(500, customMessage);

        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    void statusCodeShouldBeSetCorrectly() {
        GeneralException exception = new GeneralException(401, "Unauthorized");
        assertEquals(401, exception.getStatusCode());
    }

    @Test
    void exceptionShouldBeInstanceOfRuntimeException() {
        GeneralException exception = new GeneralException(400, "Bad Request");
        assertTrue(exception instanceof RuntimeException);
    }
}