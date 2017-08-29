package com.tikal.gettingstarted;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tikal.gettingstarted.mongo.dao.EmployeeDao;
import com.tikal.gettingstarted.mongo.entity.Employee;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestEmployeeDao {

	@Autowired
	private EmployeeDao employeeDao;
	
	@Test
	public void testFindByName() {
		Employee employee = new Employee("Test", "Testy", "Super tester");
		employee = employeeDao.insert(employee);
		
		Employee byName = employeeDao.findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName());
		Assert.assertNotNull(byName);
		
		byName = employeeDao.findByFirstNameAndLastName(employee.getLastName(), employee.getFirstName());
		Assert.assertNull(byName);
	}
}
