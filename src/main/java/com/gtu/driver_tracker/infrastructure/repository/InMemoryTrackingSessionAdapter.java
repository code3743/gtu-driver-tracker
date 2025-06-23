package com.gtu.driver_tracker.infrastructure.repository;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.gtu.driver_tracker.domain.model.TrackingSession;
import com.gtu.driver_tracker.domain.repository.TrackingSessionRepository;
@Component
public class  InMemoryTrackingSessionAdapter  implements TrackingSessionRepository {

     private final Map<Long, TrackingSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void startTrackingSession(TrackingSession trackingSession) {
        sessions.put(trackingSession.getDriverId(), trackingSession);
    }

    @Override
    public void endTrackingSession(Long driverId) {
        sessions.remove(driverId);
   }

    @Override
    public TrackingSession getTrackingSessionById(Long driverId) {
       return sessions.get(driverId);
    }

    @Override
    public boolean isTracking(Long driverId) {
        return sessions.containsKey(driverId);
    }

    @Override
    public List<TrackingSession> getAllTrackingSessions() {
        return  List.copyOf(sessions.values());   
    }
}
