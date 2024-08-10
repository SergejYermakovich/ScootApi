package com.app.scoot.util;

import com.app.scoot.dto.request.DistanceCalculateRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CacheUtil {

    private static final String CACHE_KEY_PREFIX = "distance:";
    public static String generateCacheKey(DistanceCalculateRequest request) {
        return CACHE_KEY_PREFIX + request.getTo().getLatitude() + "," +
                request.getTo().getLongitude() + "," +
                request.getFrom().getLatitude() + "," +
                request.getFrom().getLongitude();
    }
}
