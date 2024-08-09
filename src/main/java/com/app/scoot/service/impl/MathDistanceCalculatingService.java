package com.app.scoot.service.impl;

import com.app.scoot.dto.enums.CalculationMethod;
import com.app.scoot.dto.request.DistanceCalculateRequest;
import com.app.scoot.dto.response.DistanceItem;
import com.app.scoot.service.DistanceService;
import com.app.scoot.util.DistanceCalculationUtil;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class MathDistanceCalculatingService implements DistanceService {
    // здесь сделаем кэширование
    private final Lock lock = new ReentrantLock();

    @Override
    public DistanceItem getDistance(DistanceCalculateRequest request) {
        lock.lock();
        try {
            double distance = DistanceCalculationUtil.getDistanceByHaversine(
                    request.getFrom().getLatitude(),
                    request.getFrom().getLongitude(),
                    request.getTo().getLatitude(),
                    request.getTo().getLongitude());
            return new DistanceItem(distance, CalculationMethod.HAVERSINE);
        } finally {
            lock.unlock();
        }
    }
}
