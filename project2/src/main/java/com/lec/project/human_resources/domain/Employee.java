package com.lec.project.human_resources.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private int empno;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private int salary;
	
	@Column(nullable = false)
	private String departmentName;
	
	@Column(nullable = false)
	private Position position;
	
	@Column(nullable = false)
	private String address;
	
	@Column(nullable = false)
	private LocalDateTime hiredate;
	
	public void changePassword(String password) {
		this.password = password;
	}
	
	public void changeName(String name) {
		this.name = name;
	}
	
	public void changeSalary(int salary) {
		this.salary = salary;
	}
	
	public void changeDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	public void changePosition(Position position) {
		this.position = position;
	}
	
	public void changeAddress(String address) {
		this.address = address;
	}
	
	// slack Test
}