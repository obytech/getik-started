package com.tikal.gettingstarted.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tikal.gettingstarted.mongo.dao.EmployeeDao;
import com.tikal.gettingstarted.mongo.dao.LocationInfoDao;
import com.tikal.gettingstarted.mongo.entity.Employee;
import com.tikal.gettingstarted.mongo.entity.LocationInfo;
import com.tikal.gettingstarted.services.LocationService;

@Service
public class LocationServiceImpl implements LocationService {
	
	private LocationInfoDao locationInfoDao;
	
	private EmployeeDao employeeDao;
	
	private Map<String, Employee> employeeMap;
	
	@Autowired
	public LocationServiceImpl(LocationInfoDao locationInfoDao, EmployeeDao employeeDao) {
		this.locationInfoDao = locationInfoDao;
		this.employeeDao = employeeDao;
	}
	
	@PostConstruct
	public void init() {
		List<Employee> employees = employeeDao.findAll();
		employeeMap = employees.stream()
				.collect(
					Collectors.toMap(Employee::getId, Function.identity()));
	}
	
	@Override
	public void saveLocation(String employeeId, float longtitude, float latitude, float altitude) {
		Optional<Employee> optEmployee = Optional.ofNullable(employeeMap.get(employeeId));
		Employee emp = optEmployee.orElseThrow(
				() -> new IllegalArgumentException("No employee found for id"));
		
		locationInfoDao.save(new LocationInfo(latitude, longtitude, altitude, emp));
	}
}
