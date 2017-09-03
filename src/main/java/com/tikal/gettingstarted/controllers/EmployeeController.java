package com.tikal.gettingstarted.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tikal.gettingstarted.mongo.dao.EmployeeDao;
import com.tikal.gettingstarted.mongo.entity.Employee;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Bla bla")
@RestController
@RequestMapping("Employee")
@SwagMe // This is filtered by Swag config
public class EmployeeController {
	
	@Autowired
	private EmployeeDao employeeDao;

	@ApiOperation(value = "Retrive an employee", response = Employee.class)
	@ApiResponses({
		@ApiResponse(code = 404, message = "No employee found for id"),
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
		@ApiResponse(code = 406, message = "Failed to create an employee - already exists under first/last names")
	})
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> add(@RequestBody Employee employee) {
		Employee temp = employeeDao.findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName());
		if (temp != null) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}
		employee = employeeDao.save(employee);
		return ResponseEntity.status(HttpStatus.CREATED).body("/Employee/" + employee.getId());
	}
	
	@ApiOperation(value = "Remove an employee")
	@ApiResponses({
		@ApiResponse(code = 418, message = "Successfully deleted employee"),
		@ApiResponse(code = 404, message = "No employee found for id")
	})
	@DeleteMapping(value = "{id}", produces = MediaType.TEXT_PLAIN_VALUE)
	public void removeEmployee(@PathVariable("id") String employeeId, HttpServletResponse response) {
		if (!employeeDao.exists(employeeId)) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		}
		employeeDao.delete(employeeId);
		response.setStatus(HttpStatus.I_AM_A_TEAPOT.value());
	}
}
