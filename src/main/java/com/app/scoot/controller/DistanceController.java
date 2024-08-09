package com.app.scoot.controller;

import com.app.scoot.config.RateLimitConfig;
import com.app.scoot.dto.response.DistanceItem;
import com.app.scoot.dto.request.DistanceCalculateRequest;
import com.app.scoot.service.DistanceService;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static com.app.scoot.consts.ApiConsts.API;
import static com.app.scoot.consts.ApiConsts.PLATFORM_SUB_HEADER;

@RestController
@RequestMapping(API)
@RequiredArgsConstructor
public class DistanceController {

    private final DistanceService distanceService;
    private final RateLimitConfig rateLimitConfig;

    @PostMapping("/distance")
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
    @ApiResponse(responseCode = "429",
            description = "Too many requests.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)))
    public DistanceItem getDistance(@RequestHeader(PLATFORM_SUB_HEADER) UUID sub,
                                    @Valid @RequestBody DistanceCalculateRequest request) {

        RateLimiter rateLimiter = rateLimitConfig.getRateLimiterForSub(sub);

        if (rateLimiter.acquirePermission()) {
            return distanceService.getDistance(request);
        } else {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests.");
        }
    }
}
