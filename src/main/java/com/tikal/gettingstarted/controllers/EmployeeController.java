package com.tikal.gettingstarted.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tikal.gettingstarted.mongo.dao.EmployeeDao;
import com.tikal.gettingstarted.mongo.entity.Employee;

@RestController
@RequestMapping("Employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeDao employeeDao;

	@GetMapping("{id}")
	public Employee get(@PathVariable("id") String id) {
		Employee output = employeeDao.findOne(id);
		return output;
	}
	
	@PostMapping
	public ResponseEntity<String> add(@RequestBody Employee employee) {
		Employee temp = employeeDao.findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName());
		if (temp != null) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
		}
		employee = employeeDao.save(employee);
		return ResponseEntity.status(HttpStatus.CREATED).body(employee.getId());
	}
}
