package com.app.scoot.controller;

import com.app.scoot.dto.response.DistanceItem;
import com.app.scoot.dto.request.DistanceCalculateRequest;
import com.app.scoot.service.DistanceService;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.app.scoot.consts.ApiConsts.API;
import static com.app.scoot.consts.ApiConsts.DISTANCE_API_NAME;
import static com.app.scoot.consts.ApiConsts.PLATFORM_SUB_HEADER;

@RestController
@RequestMapping(API)
@RequiredArgsConstructor
public class DistanceController {

    private final DistanceService distanceService;
    private final RateLimiterConfig config = RateLimiterConfig.custom()
            .limitRefreshPeriod(Duration.ofMillis(1))
            .limitForPeriod(10)
            .timeoutDuration(Duration.ofMillis(25))
            .build();
    private final RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);
    private final ConcurrentHashMap<UUID, RateLimiter> limiters = new ConcurrentHashMap<>();

    @GetMapping("/distance")
    @Operation(summary = "Get distance between two location points",
            description = """
                    Returns distance between two location points.
                    """)
    @ApiResponse(responseCode = "200",
            description = "Successful response with distance between two location points",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "403",
            description = "",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    public DistanceItem getDistance(@RequestHeader(PLATFORM_SUB_HEADER) UUID sub,
                                    @Valid @ParameterObject DistanceCalculateRequest request) {
        RateLimiter rateLimiter = limiters.computeIfAbsent(sub, k -> rateLimiterRegistry.rateLimiter(DISTANCE_API_NAME));

        if (rateLimiter.acquirePermission()) {
            return distanceService.getDistance(request);
        } else {
            throw new RuntimeException("Too many requests.");
        }
    }
}
