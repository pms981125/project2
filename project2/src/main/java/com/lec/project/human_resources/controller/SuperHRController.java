package com.lec.project.human_resources.controller;

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
@RequestMapping("/sudo")
@RequiredArgsConstructor
public class SuperHRController {
	private final HRService hrService;
	/*	
		@GetMapping("/allUserList") // 모든 유저 목록
		public String getAllUserList(Model model) {
			List<MemberSecurityDTO> memberSecurityDTOList = hrService.getAllUserList();
			
			model.addAttribute("memberList",  memberSecurityDTOList);
			
			return "admin/allUserList";
		}*/
	
	@GetMapping("/allUserList") // 모든 유저 목록
	public String getAllUserList(@PageableDefault(page = 1) Pageable pageable, Model model, @RequestParam(name = "size", defaultValue = "10") int size, 
								 @RequestParam(name = "onlyAdmin", defaultValue = "false") boolean onlyAdmin) {
		Page<MemberSecurityDTO> pages = null;
		int limit = 5;
		int startPage; 
		int endPage; 
		
		if (onlyAdmin) {
			pages = hrService.getAdminListWithPaging(pageable.getPageNumber() - 1, pageable, size);
			startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / limit))) - 1) * limit + 1;
			endPage = Math.min(startPage + limit, pages.getTotalPages());
		} else {
			pages = hrService.getAllUserListWithPaging(pageable, size);
			startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / limit))) - 1) * limit + 1;
			endPage = Math.min(startPage + limit, pages.getTotalPages());
		}
		
		model.addAttribute("pages", pages);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("size", size);
		model.addAttribute("onlyAdmin", onlyAdmin);
		
		return "admin/allUserList";
	}
	
	@GetMapping("/userInfo") // 유저 상세정보
	public String getInfo(@RequestParam("id") String id, Model model) {
		MemberSecurityDTO memberSecurityDTO = hrService.getUser(id);
		
		model.addAttribute("member",  memberSecurityDTO);
		
		return "admin/allUserInfo";
	}
	
	@PostMapping("/update") // 유저 정보 수정 - 틀
	public String update(@RequestParam(name = "originalId") String originalId, @RequestParam(name = "newId") String newId) {
		hrService.update(originalId, newId);
		
		return "redirect:/sudo/userInfo?id=" + originalId;
	}
	
	@PostMapping("/remove") // 유저 삭제
	public String remove(@RequestParam(name = "id") String id) {
		log.info("-=- remove");
		
		hrService.remove(id);
		
		return "redirect:/sudo/allUserList";
	}
	
	@GetMapping("/addAdmin") // 관리자 추가 페이지 이동
	public String addAdmin() {
		return "admin/addAdminForm";
	}
	
	@PostMapping("/addAdmin") // 관리자 추가
	public String addAdmin(@RequestParam("id") String id, @RequestParam("password") String password, @RequestParam("name") String name,
						   @RequestParam("ssnFront") String ssnFront, @RequestParam("ssnEnd") String ssnEnd, @RequestParam("email") String email) {
		hrService.addAdmin(id, password, name, ssnFront, ssnEnd, email);
		
		return "redirect:/sudo/allUserList";
	}
	
	@PostMapping("/delegate") // SuperAdmin 위임
	public String delegateAuthority(@RequestParam("superAdminId") String superAdminId, @RequestParam("adminId") String adminId) {
		// log.info(superAdminId + "234124-013" + adminId);
		hrService.delagateAuthority(superAdminId, adminId); 
		
		// return "redirect:/sudo/userInfo?id=" + adminId;
		return "redirect:/sudo/logout";
	}
	
	@PostMapping("/initializePassword") // 비밀번호 초기화(재발급)
	public String initializePassword(@RequestParam("memberId") String memberId) {
		// log.info(memberId + " dhbahswk");

		hrService.initializePassword(memberId);
		
		return "redirect:/sudo/userInfo?id=" + memberId;
		// return "redirect:/sudo/allUserList";
	}
	
	@GetMapping("/logout")
	public String logout() {
		return "redirect:/logout";
	}
}