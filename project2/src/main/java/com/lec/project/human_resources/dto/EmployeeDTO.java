package com.lec.project.human_resources.dto;

import java.time.LocalDateTime;

import com.lec.project.human_resources.domain.DepartmentName;
import com.lec.project.human_resources.domain.Position;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
	private String id;
	private String password;
	private int employeeNumber;
	private String name;
	private int annualSalary;
	private DepartmentName departmentName;
	private Position position;
	private String address;
	private LocalDateTime localDateTime;
}
