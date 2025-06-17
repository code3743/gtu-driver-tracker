package com.gtu.driver_tracker.domain.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class LocationTest {

    @Test
    void constructorAndGettersShouldWork() {
        double latitude = 40.7128;
        double longitude = -74.0060;
        Location location = new Location(latitude, longitude);

        assertEquals(latitude, location.getLatitude());
        assertEquals(longitude, location.getLongitude());
    }

    @Test
    void setLatitudeShouldUpdateLatitude() {
        Location location = new Location(0.0, 0.0);
        location.setLatitude(51.5074);
        assertEquals(51.5074, location.getLatitude());
    }

    @Test
    void setLongitudeShouldUpdateLongitude() {
        Location location = new Location(0.0, 0.0);
        location.setLongitude(-0.1278);
        assertEquals(-0.1278, location.getLongitude());
    }

    @Test
    void toStringShouldReturnCorrectFormat() {
        Location location = new Location(10.0, 20.0);
        String expected = "latitude=10.0, longitude=20.0";
        assertEquals(expected, location.toString());
    }
}