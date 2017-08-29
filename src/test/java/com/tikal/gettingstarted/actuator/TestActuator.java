package com.tikal.gettingstarted.actuator;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.tikal.gettingstarted.GettingstartedApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GettingstartedApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestActuator {
	
	private static final String ACTUATOR_URI = "http://localhost:18080/actuator/";

	private TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testOneTimeRandom() {
		Integer randomId = restTemplate.getForObject(ACTUATOR_URI + "oneTimeRandom", Integer.class);
		Assert.assertNotNull(randomId);

		try {
			restTemplate.getForObject(ACTUATOR_URI + "oneTimeRandom", Integer.class);
		} catch (Exception e) { 
			return; 
		}
		
		throw new RuntimeException("Shouldn't have reached here!");
	}
	
	@Test 
	public void testHealth() throws URISyntaxException {
		String target = ACTUATOR_URI.replace("http://", "http://admin:admin@");
		RequestEntity<?> entity = RequestEntity.get(new URI(target + "health")).build();
		ResponseEntity<String> response = restTemplate.exchange(entity, String.class);
		Assert.assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
		Assert.assertEquals("{\"status\":\"OUT_OF_SERVICE\"}", response.getBody());
	}
}
