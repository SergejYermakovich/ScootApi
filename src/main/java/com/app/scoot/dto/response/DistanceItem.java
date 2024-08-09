package com.app.scoot.dto.response;

import com.app.scoot.dto.LocationPoint;
import com.app.scoot.dto.enums.CalculationMethod;
import com.app.scoot.dto.enums.DistanceUnit;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DistanceItem {
    private LocationPoint from;
    private LocationPoint to;
    private double distance;
    private DistanceUnit unit;
    private CalculationMethod calculationMethod;
}
