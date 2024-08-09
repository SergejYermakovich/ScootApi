package com.app.scoot.service.impl;

import com.app.scoot.dto.enums.CalculationMethod;
import com.app.scoot.dto.request.DistanceCalculateRequest;
import com.app.scoot.dto.response.DistanceItem;
import com.app.scoot.service.DistanceService;
import com.app.scoot.util.DistanceCalculationUtil;
import org.springframework.stereotype.Service;

@Service

public class MathDistanceCalculatingService implements DistanceService {
    @Override
    public DistanceItem getDistance(DistanceCalculateRequest request) {
        double distance = DistanceCalculationUtil.getDistanceByHaversine(
                request.getFrom().getLatitude(),
                request.getFrom().getLongitude(),
                request.getTo().getLatitude(),
                request.getTo().getLongitude());
        return new DistanceItem(distance, CalculationMethod.HAVERSINE);
    }
}
