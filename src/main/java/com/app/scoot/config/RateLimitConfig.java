package com.app.scoot.config;

import com.app.scoot.config.property.RateLimitProperties;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@RequiredArgsConstructor
public class RateLimitConfig {

    private final RateLimitProperties properties;
    private final Map<UUID, RateLimiter> rateLimiters = new ConcurrentHashMap<>();

    public RateLimiter getRateLimiter(final UUID sub) {
        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom()
                .limitForPeriod(properties.getLimitForPeriod())
                .limitRefreshPeriod(properties.getLimitRefreshPeriod())
                .timeoutDuration(properties.getTimeoutDuration())
                .build();

        return RateLimiter.of("rateLimiter-" + sub, rateLimiterConfig);
    }

    public RateLimiter getRateLimiterForSub(final UUID sub) {
        return rateLimiters.computeIfAbsent(sub, k -> getRateLimiter(sub));
    }
}
