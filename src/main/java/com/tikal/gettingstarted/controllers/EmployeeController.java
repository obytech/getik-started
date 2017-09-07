package com.tikal.gettingstarted.controllers;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tikal.gettingstarted.mongo.dao.EmployeeDao;
import com.tikal.gettingstarted.mongo.entity.Employee;
import com.tikal.gettingstarted.mongo.entity.LocationInfo;
import com.tikal.gettingstarted.services.LocationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Api(value = "Marker for swag annotation filter.", hidden = true)
@RestController
@RequestMapping("Employee")
//@SwagMe // This is filtered by Swag config
public class EmployeeController {
	
	private static final String EMPLOYEE_NOT_FOUND = "No employee found for id";
	
	private EmployeeDao employeeDao;
	
	private LocationService locationService;
	
	@Autowired
	public EmployeeController(EmployeeDao employeeDao, LocationService locationService) {
		this.employeeDao = employeeDao;
		this.locationService = locationService;
	}
	
	@ApiOperation(value = "Genrates UUID", hidden = false, authorizations = @Authorization("dsds"), response = LocationInfo.class)
	@GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
	public String get() {
		return UUID.randomUUID().toString();
	}

	@ApiOperation(value = "Retrive an employee", response = Employee.class)
	@ApiResponses({
		@ApiResponse(code = 404, message = EMPLOYEE_NOT_FOUND),
		@ApiResponse(code = 200, message = "Successfully retrieved employee")
	})
	@GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Employee> get(@PathVariable("id") String id) {
		Employee output = employeeDao.findOne(id);
		if (output == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(output);
	}
	
	@ApiOperation(value = "Add new employee")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Employee added successfully"),
		@ApiResponse(code = 409, message = "Failed to create an employee - already exists under first/last names")
	})
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> add(@RequestBody Employee employee) {
		Optional<Employee> existEmp = employeeDao.findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName());
		existEmp.ifPresent((emp) -> {
			throw new RuntimeException(String.format("Already exist: %s %s", emp.getFirstName(), emp.getLastName()));
		});
			
		employee = employeeDao.save(employee);
		return ResponseEntity.status(HttpStatus.CREATED).body("/Employee/" + employee.getId());
	}
	
	@ApiOperation(value = "Remove an employee")
	@ApiResponses({
		@ApiResponse(code = 418, message = "Successfully deleted employee"),
		@ApiResponse(code = 404, message = EMPLOYEE_NOT_FOUND)
	})
	@DeleteMapping(value = "{id}", produces = MediaType.TEXT_PLAIN_VALUE)
	public void removeEmployee(@PathVariable("id") String employeeId, HttpServletResponse response) throws IllegalAccessException {
		if (!employeeDao.exists(employeeId)) {
			throw new IllegalAccessException();
		}
		employeeDao.delete(employeeId);
		response.setStatus(HttpStatus.I_AM_A_TEAPOT.value());
	}
	
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Already exist")
	private void employeeAlreadyExists() {
		
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = EMPLOYEE_NOT_FOUND)
	private void employeeNotFound() {
		
	}

	@ApiOperation("Save location data")
	@ApiResponse(code = 200, message = "OK")
	@PostMapping(path = "{employeeId}/location", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addLocation(@PathVariable("employeeId") String employeeId, @RequestBody LocationInfo location) {
		locationService.saveLocation(employeeId, location.getLongtitude(), location.getLatitude(), location.getAltitude());
	}
}
