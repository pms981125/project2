package com.lec.project.human_resources.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

	@GetMapping("/userInfo") // 유저 상세정보
	public String getInfo(@RequestParam("id") String id, Model model) {
		MemberSecurityDTO memberSecurityDTO = hrService.getUser(id);
		
		model.addAttribute("member",  memberSecurityDTO);
		
		return "admin/userInfo";
	}
	/*	
		@GetMapping("/userList") // 일반 유저 목록
		public String getUserList(Model model) {
			List<MemberSecurityDTO> memberSecurityDTOList = hrService.getUserList();
			
			model.addAttribute("memberList",  memberSecurityDTOList);
			
			return "admin/userList";
		}
	 */
	
	@GetMapping("/userList") // 일반 유저 목록
	public String getUserList(@PageableDefault(page = 1) Pageable pageable, Model model) {
		Page<MemberSecurityDTO> pages = hrService.getUserListWithPaging(pageable);
		int limit = 5;
		int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / limit))) - 1) * limit + 1;
		int endPage = Math.min(startPage + limit - 1, pages.getTotalPages());
		
		model.addAttribute("pages", pages);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
		return "admin/userList";
	}
	
	@PostMapping("/update") // 유저 정보 수정 - 현재는 비밀번호만 가능
	public String update(@RequestParam(name = "id") String id, @RequestParam(name = "password") String password) {
		log.info("-=- update " + id + " " + password);

		hrService.update(id, password);
		
		return "redirect:/hr/userInfo?id=" + id;
	}
	
	@PostMapping("/remove") // 유저 삭제
	public String remove(@RequestParam(name = "id") String id) {
		log.info("-=- remove");
		
		hrService.remove(id);
		
		return "redirect:/hr/userList";
	}
}