package com.tikal.gettingstarted.mongo.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tikal.gettingstarted.mongo.entity.Employee;

public interface EmployeeDao extends MongoRepository<Employee, String> {
	
	public Optional<Employee> findByFirstNameAndLastName(String firstName, String lastName);
	
}
