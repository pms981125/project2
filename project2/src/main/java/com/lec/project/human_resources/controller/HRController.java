package com.lec.project.human_resources.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lec.project.human_resources.dto.EmployeeDTO;
import com.lec.project.human_resources.service.HRService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/hr")
@RequiredArgsConstructor
public class HRController {
	private final HRService hrService;
	
	@GetMapping("/info")
	public void getInfo(@RequestParam("id") String id) {
		EmployeeDTO employeeDTO = hrService.getInfoEmployee(id);
		log.info(employeeDTO);
	}
}