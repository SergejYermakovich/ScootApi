package com.app.scoot.integration;

import com.app.scoot.dto.LocationPoint;
import com.app.scoot.dto.request.DistanceCalculateRequest;
import com.app.scoot.dto.response.DistanceItem;
import com.app.scoot.service.DistanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.app.scoot.consts.ApiConsts.PLATFORM_SUB_HEADER;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DistanceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DistanceService distanceService;

    @MockBean
    private RateLimiterRegistry rateLimiterRegistry;

    @MockBean
    private RateLimiter rateLimiter;


    private static final DistanceCalculateRequest REQUEST = new DistanceCalculateRequest(
            new LocationPoint(40.7128, -74.0060),
            new LocationPoint(34.0522, -118.2437)
    );

    @Test
    public void testGetDistance() throws Exception {

        mockMvc.perform(post("/api/distance")
                        .header(PLATFORM_SUB_HEADER, UUID.fromString("123e4567-e89b-12d3-a456-426614174001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(REQUEST)))
                .andExpect(status().isOk());
    }

    @Test
    public void testRateLimitExceeded() throws Exception {
        UUID sub = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");

        DistanceItem distanceItem = DistanceItem.builder().distance(3935.75).build();
        when(distanceService.getDistance(any(DistanceCalculateRequest.class))).thenReturn(distanceItem);

        when(rateLimiterRegistry.rateLimiter(anyString())).thenReturn(rateLimiter);
        when(rateLimiter.acquirePermission()).thenReturn(true, true, true, true, true, true, true, true, true, true, false);

        // Sending 10 successful requests
        for (int i = 0; i < 10; i++) {

            mockMvc.perform(post("/api/distance")
                            .header(PLATFORM_SUB_HEADER, sub)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(REQUEST)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.distance").value(distanceItem.getDistance()));
        }

        // Sending the 11th request, which should be blocked
        mockMvc.perform(post("/api/distance")
                        .header(PLATFORM_SUB_HEADER, sub)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(REQUEST)))
                .andExpect(status().isTooManyRequests());

    }

}
