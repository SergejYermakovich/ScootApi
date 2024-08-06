package com.app.scoot.service;

import com.app.scoot.dto.request.DistanceCalculateRequest;
import com.app.scoot.dto.response.DistanceItem;

public interface DistanceService {
    DistanceItem getDistance(DistanceCalculateRequest request);
}
