package com.app.scoot.controller;

import com.app.scoot.dto.response.DistanceItem;
import com.app.scoot.dto.request.DistanceCalculateRequest;
import com.app.scoot.service.DistanceService;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.app.scoot.consts.ApiConsts.API;

@RestController
@RequestMapping(API)
@RequiredArgsConstructor
public class DistanceController {

    private final DistanceService distanceService;

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
    public DistanceItem getDistance(@Valid @ParameterObject DistanceCalculateRequest request) {
        return distanceService.getDistance(request);
    }
}
