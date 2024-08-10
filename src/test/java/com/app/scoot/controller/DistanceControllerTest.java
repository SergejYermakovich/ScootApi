package com.app.scoot.controller;

import com.app.scoot.config.RateLimitConfig;
import com.app.scoot.dto.LocationPoint;
import com.app.scoot.dto.enums.CalculationMethod;
import com.app.scoot.dto.enums.DistanceUnit;
import com.app.scoot.dto.response.DistanceItem;
import com.app.scoot.dto.request.DistanceCalculateRequest;
import com.app.scoot.service.DistanceService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DistanceControllerTest {

    @Mock
    private DistanceService distanceService;

    @Mock
    private RateLimitConfig rateLimitConfig;

    @Mock
    private RateLimiterRegistry rateLimiterRegistry;

    @Mock
    private RateLimiter rateLimiter;

    @InjectMocks
    private DistanceController distanceController;

    private UUID sub;
    private DistanceCalculateRequest request;

    @BeforeEach
    public void setUp() {
        sub = UUID.randomUUID();
        request = new DistanceCalculateRequest(
                new LocationPoint(40.7128, -74.0060),
                new LocationPoint(34.0522, -118.2437));

        when(rateLimitConfig.getRateLimiterForSub(any())).thenReturn(rateLimiter);
    }

    @Test
    public void testGetDistanceSuccess() {

        when(rateLimiter.acquirePermission()).thenReturn(true);

        DistanceItem expectedDistance = DistanceItem.builder()
                .from(request.getFrom())
                .to(request.getTo())
                .distance(100)
                .unit(DistanceUnit.KILOMETER)
                .calculationMethod(CalculationMethod.HAVERSINE)
                .build();
        when(distanceService.getDistance(any(DistanceCalculateRequest.class))).thenReturn(expectedDistance);

        DistanceItem actualDistance = distanceController.getDistance(sub, request);

        assertEquals(expectedDistance, actualDistance);
        verify(distanceService).getDistance(request);
    }

    @Test
    public void testGetDistanceTooManyRequests() {
        when(rateLimiter.acquirePermission()).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                distanceController.getDistance(sub, request));

        assertEquals(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase(), exception.getReason());
        verify(distanceService, never()).getDistance(any(DistanceCalculateRequest.class));
    }
}
