package com.app.scoot.dto.response;

import com.app.scoot.dto.enums.CalculationMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DistanceItem {
    private double distance;
    private CalculationMethod calculationMethod;
}
