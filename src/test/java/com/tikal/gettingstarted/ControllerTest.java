package com.tikal.gettingstarted;


import java.util.Optional;

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
import com.tikal.gettingstarted.mongo.entity.Employee;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GettingstartedApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int serverPort;

	@MockBean
	private EmployeeDao dao;
	
	private Employee mocked;

	private String target;

	@Before
	public void init() {
		target = "http://localhost:" + serverPort + "/Employee/";
		mocked = new Employee("test first", "test last", "super tester");
		mocked.setId("123");
	}

	@Test
	public void testEmployeeCreation() throws Exception {
		String generatedId = "123456";
		Employee employee = new Employee("test f", "test l", "super tester");
		mocked.setId(generatedId);
		Mockito.when(dao.findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName())).thenReturn(Optional.empty());
		Mockito.when(dao.save(employee)).thenReturn(mocked);
		
		ResponseEntity<String> response = restTemplate.postForEntity(target, employee, String.class);
		Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
		Assert.assertEquals("/Employee/" + generatedId, response.getBody());
	}
	
	@Test
	public void testEmployeeCreationFailure() throws Exception {
		Employee employee = new Employee("test f", "test l", "failure gen");
		Mockito.when(dao.findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName())).thenReturn(Optional.of(employee));
		
		ResponseEntity<String> response = restTemplate.postForEntity(target, employee, String.class);
		Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_ACCEPTABLE);
	}

	@Test
	public void testFindEmployee() {
		String id = "123";
		Mockito.when(dao.findOne(id)).thenReturn(mocked); 
		Employee emp = restTemplate.getForObject(target + id, Employee.class);
		Assert.assertTrue(emp.equals(mocked));
	}
	
	@Test
	public void testEmployeeNotFound() {
		String id = "1234";
		Mockito.when(dao.findOne(id)).thenReturn(null); 
		Employee emp = restTemplate.getForObject(target + id, Employee.class);
		Assert.assertNull(emp);
	}
}
