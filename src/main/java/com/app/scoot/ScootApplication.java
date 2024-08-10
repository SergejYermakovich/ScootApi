package com.app.scoot;

import com.app.scoot.config.property.RateLimitProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RateLimitProperties.class)
public class ScootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScootApplication.class, args);
	}

}
