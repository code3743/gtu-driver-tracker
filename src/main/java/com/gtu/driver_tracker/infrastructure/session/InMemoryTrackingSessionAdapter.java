package com.gtu.driver_tracker.infrastructure.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.gtu.driver_tracker.domain.exception.ResourceNotFoundException;
import com.gtu.driver_tracker.domain.model.TrackingSession;
import com.gtu.driver_tracker.domain.service.TrackingSessionPort;

public class  InMemoryTrackingSessionAdapter  implements TrackingSessionPort {

     private final Map<Long, TrackingSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void startTrackingSession(Long driverId) {
        TrackingSession session = new TrackingSession(driverId);
        sessions.put(driverId, session);
    }

    @Override
    public void endTrackingSession(Long driverId) {
        sessions.remove(driverId);
   }

    @Override
    public TrackingSession getTrackingSessionById(Long sessionId) {
       var session = sessions.get(sessionId);
        if (session != null) {
            return session;
        } else {
            throw new ResourceNotFoundException("Tracking session not found for ID: " + sessionId);
        }
    }

    @Override
    public boolean isTracking(Long driverId) {
        return sessions.containsKey(driverId);
    }

}
