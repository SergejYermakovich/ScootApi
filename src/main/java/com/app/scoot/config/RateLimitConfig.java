package com.app.scoot.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@AllArgsConstructor
public class RateLimitConfig {

    private final Map<UUID, RateLimiter> rateLimiters = new ConcurrentHashMap<>();

    public RateLimiter getRateLimiter(final UUID sub) {
        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom()
                .limitForPeriod(10)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ofMillis(25))
                .build();

        return RateLimiter.of("rateLimiter-" + sub, rateLimiterConfig);
    }

    public RateLimiter getRateLimiterForSub(final UUID sub) {
        return rateLimiters.computeIfAbsent(sub, k -> getRateLimiter(sub));
    }
}
