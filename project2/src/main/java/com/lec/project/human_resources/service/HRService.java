package com.lec.project.human_resources.service;

import com.lec.project.human_resources.dto.EmployeeDTO;

public interface HRService {
	EmployeeDTO getInfoEmployee(String id);
}