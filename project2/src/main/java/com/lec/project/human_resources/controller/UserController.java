package com.lec.project.human_resources.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@GetMapping("/login")
	public String login(Model model, @RequestParam(value = "error", defaultValue = "false") boolean error, @RequestParam(value = "exception", defaultValue = "") String exception) {
		model.addAttribute("error", error);
		model.addAttribute("exception", exception);
		
		if (error) {
			return "user/login?error=true&exception" + exception;
		} else {
			return "user/login";
		}
	}
	
	/*	
	@GetMapping("/home")
	public String goHome() {
		return "./index.html";
	}
	*/
	
	@GetMapping("/userInfo")
	public String getInfo(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		MemberSecurityDTO member = hrService.getUser(username);
		
		model.addAttribute("member", member);
		
		return "user/userInfo";
	}
	
	@PostMapping("/update")
	public String update(@RequestParam(name = "id") String id, @RequestParam(name = "name") String name, @RequestParam(name = "ssn") String ssn, @RequestParam(name = "phone") String phone,
			 			 @RequestParam(name = "email") String email, @RequestParam(name = "address") String address, @RequestParam(name = "annualSalary", defaultValue = "0") String annualSalary) {
		int salary = Integer.parseInt(annualSalary);

		hrService.update(id, name, ssn, phone, email, address, salary);
		
		return "redirect:/user/userInfo?id=" + id;
	}
	
	@PostMapping("/withdrawal")
	public String remove(@RequestParam(name = "id") String id, @RequestParam(name = "isAdmin", defaultValue = "false") boolean isAdmin) {
		hrService.remove(id, isAdmin);
		
		return "redirect:/user/logout";
	}
	
	@GetMapping("/goRegisterForm")
	public String goRegisterForm() {
		return "user/signUp";
	}
	
	@PostMapping("/register")
	public String Register(@RequestParam("id") String id, @RequestParam("password") String password, @RequestParam("name") String name, @RequestParam("ssn1") String ssn1, @RequestParam("ssn2") String ssn2, 
						   @RequestParam("phone1") String phone1, @RequestParam("phone2") String phone2, @RequestParam("email") String email, @RequestParam("location") String location, @RequestParam("address") String address) {
		String ssn = ssn1 + "-" + ssn2;
		String phone = "010-" + phone1 + "-" + phone2;
		
		hrService.addMember(id, password, name, ssn, phone, email, address, location); // 중복 체크
		
		return "redirect:/user/logout"; // 수정 필요?
	}
	
	
	@GetMapping("/logout")
	public String logout() {
		return "redirect:/logout";
	}
}