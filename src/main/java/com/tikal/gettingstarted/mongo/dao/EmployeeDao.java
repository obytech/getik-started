package com.tikal.gettingstarted.mongo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tikal.gettingstarted.mongo.entity.Employee;

public interface EmployeeDao extends MongoRepository<Employee, String> {
	
	public Employee findByFirstNameAndLastName(String firstName, String lastName);

}
