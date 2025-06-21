package com.gtu.driver_tracker.utils;
import java.time.Duration;
import java.time.Instant;


public class GpsUtils {

    private GpsUtils() {
    }

    public static double calculateSpeed(Instant previous,
                                           double lat1, double lon1,
                                           double lat2, double lon2) {
      
       
        long millis = Duration.between(previous, Instant.now()).toMillis();
        double hours = millis / 3_600_000.0;

       if (hours == 0) {
            return 0; 
        }

        double distanceInKilometers = calculateDistance(lat1, lon1, lat2, lon2);

    
        return distanceInKilometers / hours;
    }

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(rLat1) * Math.cos(rLat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
