package com.lec.project.human_resources.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import com.lec.project.human_resources.domain.DepartmentName;
import com.lec.project.human_resources.domain.Employee;
import com.lec.project.human_resources.domain.Position;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class EmployeeRepositoryTests {
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Test
	public void insertEmployee() {
		String[] nums = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
		String employeeNumberString = "";
		
		employeeNumberString += nums[(int) (Math.random() * 9)];
		
		for (int i = 1; i < 9; i++) {
			employeeNumberString += nums[(int) (Math.random() * 10)];
		}
		
		int employeeNumber = Integer.parseInt(employeeNumberString);
		
		Employee employee = Employee.builder().id("testId")
											  .password("testPassword")
											  .employeeNumber(employeeNumber)
											  .name("testName")
											  .annualSalary(3000)
											  .departmentName(DepartmentName.HUMAN_RESOURCES)
											  .position(Position.STAFF)
											  .address("서울시 테슽구")
											  .hiredate(LocalDateTime.now())
											  .build();
		
		employeeRepository.save(employee);
	}
}