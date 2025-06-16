package com.gtu.driver_tracker.domain.model;

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

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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

   @Override
    public String toString() {
        return "latitude=" + latitude + ", longitude=" + longitude;
    }
}