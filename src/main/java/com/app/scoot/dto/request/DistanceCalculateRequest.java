package com.app.scoot.dto.request;

import com.app.scoot.dto.LocationPoint;
import lombok.Data;

@Data
public class DistanceCalculateRequest {

    private LocationPoint from;

    private LocationPoint to;
}
