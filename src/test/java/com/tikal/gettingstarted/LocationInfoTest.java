package com.tikal.gettingstarted;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.tikal.gettingstarted.mongo.dao.EmployeeDao;
import com.tikal.gettingstarted.mongo.dao.LocationInfoDao;
import com.tikal.gettingstarted.mongo.entity.Employee;
import com.tikal.gettingstarted.mongo.entity.LocationInfo;
import com.tikal.gettingstarted.services.LocationService;
import com.tikal.gettingstarted.services.impl.LocationServiceImpl;


@RunWith(SpringRunner.class)
@SpringBootTest(classes  = GettingstartedApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class LocationInfoTest {

	@MockBean
	private EmployeeDao employeeDao;
	
	@Autowired
	private LocationInfoDao locationInfoDao;
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@LocalServerPort
	private int port;
	
	private Employee emp;
	
	private SecureRandom random = new SecureRandom();
	
	@Before
	public void init() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		emp = new Employee("test emp fn", "test emp ln", "My profession");
		emp.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		
		Map<String, Employee> anEmp = new HashMap<>();
		anEmp.put(emp.getId(), emp);
		
		Field map = LocationServiceImpl.class.getDeclaredField("employeeMap");
		map.setAccessible(true);
		map.set(locationService, anEmp);
	}
	
	@After
	public void cleanUp() {
		locationInfoDao.deleteByTrackedEmployeeId(emp.getId());
	}
	
	private LocationInfo generateLocationInfo() {
		return new LocationInfo(random.nextFloat(), random.nextFloat(), random.nextFloat(), emp);
	}
	
	@Test
	public void testLoad() {
		int iterations = random.nextInt(100000);
		exec(()-> locationInfoDao.save(generateLocationInfo()), iterations);
		
		long quantities = locationInfoDao.countByTrackedEmployeeId(emp.getId());
		Assert.assertEquals(iterations, quantities);
	}
	
	@Test
	public void testEmployeeNotFound() {
		String endpoint = "http://localhost:" + port + "/Employee/123/location";
		ResponseEntity<Void> response = restTemplate.postForEntity(endpoint, generateLocationInfo(), Void.class);
		Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void testLoadRest() {
		List<Employee> employees = new ArrayList<>(1);
		employees.add(emp);
		Mockito.when(employeeDao.findAll()).thenReturn(employees).thenThrow(new RuntimeException("Multiple init"));
		
		String endpoint = "http://localhost:" + port + "/Employee/" + emp.getId() + "/location";
		ExecutorService	executor = Executors.newFixedThreadPool(100);
		int iterations = random.nextInt(100000);
		exec(() -> {
			Future<HttpStatus> result = executor.submit(() -> {
				ResponseEntity<Void> response = restTemplate.postForEntity(endpoint, generateLocationInfo(), Void.class);
				return response.getStatusCode();
			});
			try {
				Assert.assertEquals(HttpStatus.OK, result.get());
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		}, iterations);
	}
	
	@Test
	public void testService() {
		List<Employee> employees = new ArrayList<>(1);
		employees.add(emp);
		Mockito.when(employeeDao.findAll()).thenReturn(employees).thenThrow(new RuntimeException("Multiple init"));

		locationService.saveLocation(emp.getId(), random.nextFloat(), random.nextFloat(), random.nextFloat());
	}
	
	private void exec(ExecProcedure exec, int iterations) {
		for (int counter = 0; counter < iterations; counter++) {
			exec.exec();
		}
	}

	private static interface ExecProcedure {
		public void exec();
	}
}
