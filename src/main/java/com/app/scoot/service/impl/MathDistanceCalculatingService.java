package com.app.scoot.service.impl;

import com.app.scoot.dto.enums.CalculationMethod;
import com.app.scoot.dto.enums.DistanceUnit;
import com.app.scoot.dto.request.DistanceCalculateRequest;
import com.app.scoot.dto.response.DistanceItem;
import com.app.scoot.service.DistanceService;
import com.app.scoot.util.CacheUtil;
import com.app.scoot.util.DistanceCalculationUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class MathDistanceCalculatingService implements DistanceService {
    private final Cache<String, Double> distanceCache;
    private final Lock lock = new ReentrantLock();

    public MathDistanceCalculatingService() {
        this.distanceCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(1000)
                .build();
    }

    @Override
    public DistanceItem getDistance(DistanceCalculateRequest request) {
        lock.lock();
        try {
            String key = CacheUtil.generateCacheKey(request);
            Double cachedDistance = distanceCache.getIfPresent(key);

            if (cachedDistance != null) {
                return DistanceItem.builder()
                        .from(request.getFrom())
                        .to(request.getTo())
                        .distance(cachedDistance)
                        .unit(DistanceUnit.KILOMETER)
                        .calculationMethod(CalculationMethod.HAVERSINE)
                        .build();
            }

            double distance = DistanceCalculationUtil.getDistanceByHaversine(
                    request.getFrom().getLatitude(),
                    request.getFrom().getLongitude(),
                    request.getTo().getLatitude(),
                    request.getTo().getLongitude());

            distanceCache.put(key, distance);
            return DistanceItem.builder()
                    .from(request.getFrom())
                    .to(request.getTo())
                    .distance(distance)
                    .unit(DistanceUnit.KILOMETER)
                    .calculationMethod(CalculationMethod.HAVERSINE)
                    .build();
        } finally {
            lock.unlock();
        }
    }
}
