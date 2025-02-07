package com.lec.project.human_resources.controller;

import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lec.project.MemberSecurityDTO;
import com.lec.project.human_resources.dto.CalendarDTO;
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
	
	@PostMapping("/update") // 유저 정보 수정
	public String update(@RequestParam(name = "id") String id, @RequestParam(name = "name") String name, @RequestParam(name = "ssn") String ssn, @RequestParam(name = "phone") String phone,
						 @RequestParam(name = "email") String email, @RequestParam(name = "address") String address, @RequestParam(name = "annualSalary", defaultValue = "0") String annualSalary) {
		int salary = Integer.parseInt(annualSalary);
		
		hrService.update(id, name, ssn, phone, email, address, salary);
		
		return "redirect:/sudo/userInfo?id=" + id;
	}
	
	@PostMapping("/remove") // 유저 삭제
	public String remove(@RequestParam(name = "id") String id, @RequestParam(name = "isAdmin", defaultValue = "false") boolean isAdmin) {
		if (isAdmin) {
			hrService.remove(id, isAdmin);
		} else {
			hrService.remove(id, isAdmin);
		}
		
		return "redirect:/sudo/allUserList";
	}
	
	@GetMapping("/addAdmin") // 관리자 추가 페이지 이동
	public String addAdmin() {
		return "admin/addAdminForm";
	}
	
	@PostMapping("/addAdmin") // 관리자 추가
	public String addAdmin(@RequestParam("id") String id, @RequestParam("password") String password, @RequestParam("name") String name,
						   @RequestParam("ssnFront") String ssnFront, @RequestParam("ssnEnd") String ssnEnd, @RequestParam("phone1") String phone1, @RequestParam("phone2") String phone2,   
						   @RequestParam("email") String email, @RequestParam("location") String location, @RequestParam("address") String address, @RequestParam("annualSalary") int annualSalary,
						   @RequestParam("job") String job) {
		String ssn = ssnFront + "-" + ssnEnd;
		String phone = "010-" + phone1 + "-" + phone2;
		
		hrService.addAdmin(id, password, name, ssn, phone, email, address, annualSalary, location, job);
		
		return "redirect:/sudo/allUserList";
	}
	
	@PostMapping("/delegate") // SuperAdmin 위임
	public String delegateAuthority(@RequestParam("superAdminId") String superAdminId, @RequestParam("id") String id) {
		hrService.delagateAuthority(superAdminId, id); 
		
		// return "redirect:/sudo/userInfo?id=" + adminId;
		return "redirect:/sudo/logout";
	}
	
	@PostMapping("/initializePassword") // 비밀번호 초기화(재발급)
	public String initializePassword(@RequestParam("id") String id) throws AddressException, MessagingException {
		hrService.initializePassword(id);
		
		return "redirect:/sudo/userInfo?id=" + id;
		// return "redirect:/sudo/allUserList";
	}
	
	@PostMapping("/exaltation") // 관리자 승격
	public String exaltation(@RequestParam("id") String id, @RequestParam("annualSalary") String annualSalary) {
		int salary = Integer.parseInt(annualSalary);
		
		hrService.exaltation(id, salary);
		
		return "redirect:/sudo/userInfo?id=" + id;
		// return "redirect:/sudo/allUserList";
	}
	
	@GetMapping("/logout")
	public String logout() {
		return "redirect:/logout";
	}
	
	@GetMapping("/goAttendance")
	public String goAttendance() {
		return "admin/attendance";
	}
	
	@PostMapping("/attendance")
	public String attendance(@RequestParam(name = "id") String id) {
		LocalTime localTime = LocalTime.now();
		
		// log.info("345678jhrebr " + id + " " + localTime);
		hrService.attendance(id, localTime);
		
		return "admin/attendance";
	}
	
	@PostMapping("/leave")
	public String leave(@RequestParam(name = "id") String id) {
		LocalTime localTime = LocalTime.now();
		
		hrService.leave(id, localTime);
		
		return "admin/attendance";
	}
	
	@GetMapping("/getWorkLog")
	public ResponseEntity<CalendarDTO[]> getWorkLog(@RequestParam(name = "id") String id) {
		CalendarDTO[] calendar = hrService.getWorkLog(id);
		
		return new ResponseEntity<>(calendar, HttpStatus.OK);
	}
}