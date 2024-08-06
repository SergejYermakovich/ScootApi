package com.app.scoot.service.impl;

import com.app.scoot.dto.request.DistanceCalculateRequest;
import com.app.scoot.dto.response.DistanceItem;
import com.app.scoot.service.DistanceService;
import org.springframework.stereotype.Service;

@Service
public class MathDistanceCalculatingService implements DistanceService {
    @Override
    public DistanceItem getDistance(DistanceCalculateRequest request) {
        return new DistanceItem();
    }
}
