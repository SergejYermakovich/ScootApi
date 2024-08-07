package com.app.scoot.util;

public class DistanceCalculationUtil {
    public static final double EARTH_RADIUS_KM = 6371.01;

    public static double getDistanceByHaversine(double startLatitude,
                                                double startLongitude,
                                                double endLatitude,
                                                double endLongitude) {
        double deltaLatitude = Math.toRadians(endLatitude - startLatitude);
        double deltaLongitude = Math.toRadians(endLongitude - startLongitude);

        double a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2) +
                Math.cos(Math.toRadians(startLatitude)) * Math.cos(Math.toRadians(endLatitude)) *
                        Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
