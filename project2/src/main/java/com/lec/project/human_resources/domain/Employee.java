package com.lec.project.human_resources.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	/*@Column(nullable = false)*/
	private String id;
	
	@Column(nullable = false)
	private String password;
	
	/*	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employeeNumber;
	*/
	@Column(nullable = false)
	private int employeeNumber;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private int annualSalary;
	
	@Column(nullable = false)
	private DepartmentName departmentName;
	
	@Column(nullable = false)
	private Position position;
	
	@Column(nullable = false)
	private String address;
	
	@Column(nullable = false)
	private Date hiredate;
	
	public void changePassword(String password) {
		this.password = password;
	}
	
	public void changeName(String name) {
		this.name = name;
	}
	
	public void changeSalary(int annualSalary) {
		this.annualSalary = annualSalary;
	}
	
	public void changeDepartmentName(DepartmentName departmentName) {
		this.departmentName = departmentName;
	}
	
	public void changePosition(Position position) {
		this.position = position;
	}
	
	public void changeAddress(String address) {
		this.address = address;
	}
}