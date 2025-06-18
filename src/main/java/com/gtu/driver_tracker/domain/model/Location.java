package com.gtu.driver_tracker.domain.model;

import java.time.Instant;
/**
 * Represents a geographical location with latitude and longitude coordinates.
 * <p>
 * This class provides methods to access and modify the latitude and longitude values.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     Location location = new Location(40.7128, -74.0060);
 *     double lat = location.getLatitude();
 *     double lon = location.getLongitude();
 * </pre>
 */
public class Location {
    private double latitude;
    private double longitude;
    private Instant timestamp;

    public Location(double latitude, double longitude, Instant timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}