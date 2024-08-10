package com.app.scoot.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Primary
@Getter
@Setter
@ConfigurationProperties(prefix = "rate-limiter")
public class RateLimitProperties {
    private int limitForPeriod;
    private Duration limitRefreshPeriod;
    private Duration timeoutDuration;
}
