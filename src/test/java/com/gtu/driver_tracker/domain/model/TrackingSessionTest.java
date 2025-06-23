package com.gtu.driver_tracker.domain.model;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;




class TrackingSessionTest {

    @Test
    void constructorShouldInitializeFieldsCorrectly() {
        Driver mockDriver = mock(Driver.class);
        when(mockDriver.getId()).thenReturn(123L);
        when(mockDriver.getName()).thenReturn("John Doe");

        Instant before = Instant.now();
        TrackingSession session = new TrackingSession(mockDriver);
        Instant after = Instant.now();

        assertEquals(123L, session.getDriverId());
        assertEquals("John Doe", session.getDriverName());
        assertNotNull(session.getCreationTime());
        assertFalse(session.getCreationTime().isBefore(before));
        assertFalse(session.getCreationTime().isAfter(after));
    }

    @Test
    void getSessionIdShouldReturnSessionId() {
        Driver mockDriver = mock(Driver.class);
        when(mockDriver.getId()).thenReturn(456L);
        when(mockDriver.getName()).thenReturn("Jane Smith");

        TrackingSession session = new TrackingSession(mockDriver);
        assertEquals(456L, session.getDriverId());
    }

    @Test
    void getDriverNameShouldReturnDriverName() {
        Driver mockDriver = mock(Driver.class);
        when(mockDriver.getId()).thenReturn(789L);
        when(mockDriver.getName()).thenReturn("Alice");

        TrackingSession session = new TrackingSession(mockDriver);
        assertEquals("Alice", session.getDriverName());
    }

    @Test
    void getCreationTimeShouldReturnNonNullInstant() {
        Driver mockDriver = mock(Driver.class);
        when(mockDriver.getId()).thenReturn(1L);
        when(mockDriver.getName()).thenReturn("Bob");

        TrackingSession session = new TrackingSession(mockDriver);
        assertNotNull(session.getCreationTime());
    }
}