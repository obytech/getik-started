package com.tikal.gettingstarted.actuator;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.stereotype.Component;

@Component
public class OneTimeRandomIntEndpoint implements Endpoint<Integer> {
	
	private AtomicBoolean alreadyAccessed = new AtomicBoolean(false);
	
	private SecureRandom randomizer = new SecureRandom();

	@Override
	public String getId() {
		return "oneTimeRandom";
	}

	@Override
	public boolean isEnabled() {
		return !alreadyAccessed.get();
	}

	@Override
	public boolean isSensitive() {
		return false;
	}

	@Override
	public Integer invoke() {
		alreadyAccessed.set(true);
		return randomizer.nextInt();
	}

}
