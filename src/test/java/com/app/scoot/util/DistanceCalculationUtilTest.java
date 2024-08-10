package com.app.scoot.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DistanceCalculationUtilTest {

    @Test
    public void testGetDistanceByHaversine() {
        double nycLatitude = 40.7128;
        double nycLongitude = -74.0060;
        double laLatitude = 34.0522;
        double laLongitude = -118.2437;
        double expectedDistance = 3940.0;

        double actualDistance = DistanceCalculationUtil.getDistanceByHaversine(
                nycLatitude, nycLongitude, laLatitude, laLongitude);

        assertEquals(expectedDistance, actualDistance, 10.0);
    }

    @Test
    public void testGetDistanceByHaversine_SameLocation() {
        double latitude = 52.5200;
        double longitude = 13.4050;

        double expectedDistance = 0.0;

        double actualDistance = DistanceCalculationUtil.getDistanceByHaversine(
                latitude, longitude, latitude, longitude);

        assertEquals(expectedDistance, actualDistance, 0.01);
    }
}
