package com.lec.project.human_resources.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
		return "user/userInfo";
	}
	
	@PostMapping("/update")
	public String update(@RequestParam(name = "id") String id, @RequestParam(name = "password") String password) {
		log.info("-=- userUpdate " + id + " " + password);

		// hrService.update(id, password);
		
		return "redirect:/user/userInfo?id=" + id;
	}
	
	@PostMapping("/withdrawal")
	public String remove(@RequestParam(name = "id") String id) {
		log.info("-=- withdrawal");
		
		hrService.remove(id);
		
		return "redirect:/user/logout";
	}
	
	@GetMapping("/goRegisterForm")
	public String goRegisterForm() {
		return "user/signUp";
	}
	
	@PostMapping("/register")
	public String Register(@RequestParam("id") String id, @RequestParam("password") String password, @RequestParam("location") String location) {
		log.info(id + "\n" + password + "\n" + location);
		
		return "redirect:/user/logout"; // 수정 필요
	}
	
	@GetMapping("/logout")
	public String logout() {
		return "redirect:/logout";
	}
}