package com.gtu.driver_tracker.infrastructure.repository;

import com.gtu.driver_tracker.domain.model.Driver;
import com.gtu.driver_tracker.domain.model.TrackingSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTrackingSessionAdapterTest {

    private InMemoryTrackingSessionAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new InMemoryTrackingSessionAdapter();
    }

    @Test
    void startTrackingSession_shouldStoreSession() {
        TrackingSession session = new TrackingSession(new Driver(1L, "John Doe"));
        adapter.startTrackingSession(session);

        assertEquals(session, adapter.getTrackingSessionById(1L));
    }

    @Test
    void endTrackingSession_shouldRemoveSession() {
        TrackingSession session = new TrackingSession(new Driver(2L, "Jane Doe"));
        adapter.startTrackingSession(session);

        adapter.endTrackingSession(2L);

        assertNull(adapter.getTrackingSessionById(2L));
    }

    @Test
    void getTrackingSessionById_shouldReturnCorrectSession() {
        TrackingSession session1 = new TrackingSession(new Driver(3L, "Alice"));
        TrackingSession session2 = new TrackingSession(new Driver(4L, "Bob"));
        adapter.startTrackingSession(session1);
        adapter.startTrackingSession(session2);

        assertEquals(session2, adapter.getTrackingSessionById(4L));
    }

    @Test
    void isTracking_shouldReturnTrueIfSessionExists() {
        TrackingSession session = new TrackingSession(new Driver(5L, "Charlie"));
        adapter.startTrackingSession(session);

        assertTrue(adapter.isTracking(5L));
    }

    @Test
    void isTracking_shouldReturnFalseIfSessionDoesNotExist() {
        assertFalse(adapter.isTracking(999L));
    }

    @Test
    void getAllTrackingSessions_shouldReturnAllSessions() {
        TrackingSession session1 = new TrackingSession(new Driver(6L, "David"));
        TrackingSession session2 = new TrackingSession(new Driver(7L, "Eve"));
        adapter.startTrackingSession(session1);
        adapter.startTrackingSession(session2);

        List<TrackingSession> sessions = adapter.getAllTrackingSessions();

        assertEquals(2, sessions.size());
        assertTrue(sessions.contains(session1));
        assertTrue(sessions.contains(session2));
    }
}
