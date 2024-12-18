package com.lec.project.human_resources.controller;

import java.util.List;

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
@RequestMapping("/hr")
@RequiredArgsConstructor
public class HRController {
	private final HRService hrService;
	
	@GetMapping("/userInfo")
	public String getInfo(@RequestParam("id") String id, Model model) {
		MemberSecurityDTO memberSecurityDTO = hrService.getUser(id);
		
		model.addAttribute("member",  memberSecurityDTO);
		
		return "admin/userInfo";
	}
	
	@GetMapping("/userList")
	public String getUserList(Model model) {
		List<MemberSecurityDTO> memberSecurityDTOList = hrService.getUserList();
		
		model.addAttribute("memberList",  memberSecurityDTOList);
		
		return "admin/userList";
	}
	
	@PostMapping("/update")
	public String update(@RequestParam(name = "id") String id, @RequestParam(name = "password") String password) {
		log.info("-=- update " + id + " " + password);

		hrService.update(id, password);
		
		return "redirect:/hr/userInfo?id=" + id;
	}
	
	@PostMapping("/remove")
	public String remove(@RequestParam(name = "id") String id) {
		log.info("-=- remove");
		
		hrService.remove(id);
		
		return "redirect:/hr/userList";
	}
}