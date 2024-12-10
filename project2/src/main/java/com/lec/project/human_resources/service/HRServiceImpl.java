package com.lec.project.human_resources.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.lec.project.human_resources.domain.Employee;
import com.lec.project.human_resources.dto.EmployeeDTO;
import com.lec.project.human_resources.repository.EmployeeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class HRServiceImpl implements HRService {
	private final EmployeeRepository employeeRepository;
	private final ModelMapper modelMapper;
	
	@Override
	public EmployeeDTO getInfoEmployee(String id) {
		Optional<Employee> result = employeeRepository.findById(id);
		Employee employee = result.orElseThrow();
		EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);
		
		return employeeDTO;
	}
}