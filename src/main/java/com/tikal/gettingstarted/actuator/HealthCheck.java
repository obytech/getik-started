package com.tikal.gettingstarted.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class HealthCheck implements HealthIndicator {

	@Override
	public Health health() {
		return Health.outOfService().withDetail("Generated_Error", "Some error").build();
	}

}
