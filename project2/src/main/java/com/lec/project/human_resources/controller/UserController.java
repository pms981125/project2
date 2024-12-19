package com.lec.project.human_resources.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lec.project.MemberSecurityDTO;
import com.lec.project.human_resources.service.HRService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	private final HRService hrService;
	
	@GetMapping("/home")
	public String goHome() {
		return "user/index";
	}
	
	@GetMapping("/userInfo")
	public String getInfo(Model model) {
		/*		MemberSecurityDTO memberSecurityDTO = hrService.getUser(id);
				
				model.addAttribute("member",  memberSecurityDTO);*/
		
		return "user/userInfo";
	}
}