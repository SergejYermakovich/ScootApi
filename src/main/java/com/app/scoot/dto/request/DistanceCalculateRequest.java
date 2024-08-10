package com.app.scoot.dto.request;

import com.app.scoot.dto.LocationPoint;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DistanceCalculateRequest {
    private LocationPoint from;
    private LocationPoint to;
}
