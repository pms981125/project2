package com.lec.project.human_resources.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lec.project.MemberSecurityDTO;
import com.lec.project.human_resources.service.HRService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/hr")
@RequiredArgsConstructor
public class HRController {
	private final HRService hrService;
	/*	
		@GetMapping("/info")
		public String getInfo(@RequestParam("id") String id) {
			EmployeeDTO employeeDTO = hrService.getInfoEmployee(id);
			log.info(employeeDTO);
			
			return "/admin/admin";
		}*/
	
	// @PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/userInfo")
	public String getInfo() {
		
		return "/admin/userInfo";
	}
	
	@GetMapping("/userList")
	public String getUserList(Model model) {
		List<MemberSecurityDTO> memberSecurityDTOList = hrService.getUserList();
		
		model.addAttribute("memberList",  memberSecurityDTOList);
		
		return "admin/userList";
	}
}