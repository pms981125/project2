package com.lec.project.human_resources.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lec.project.Member;
import com.lec.project.MemberRepository;
import com.lec.project.MemberRole;
import com.lec.project.human_resources.domain.Admin;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class AdminRepositoryTests {
	@Autowired
	AdminRepository adminRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Test
	public void addAdmin() { // 사용 X
		String[] nums = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
		String numberString = "";
		
		numberString += nums[(int) (Math.random() * 9)];
		
		for (int i = 1; i < 9; i++) {
			numberString += nums[(int) (Math.random() * 10)];
		}

		String id = "admin";
		String password = "a";
		int no = Integer.parseInt(numberString);
		
		Admin admin = Admin.builder().id(id)
									 .password(password)
									 .no(no)
									 .name("admin")
									 .ssn("123456-7890123")
									 .email("admin@gmail.com")
									 .build();
		
		adminRepository.save(admin);
		
		Member member = Member.builder().id(id)
										.password(password)
										.build();
		
		member.addRole(MemberRole.ADMIN);
		
		memberRepository.save(member);
	}
	
	@Test
	public void addAdminWithEncoder() {
		String[] nums = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
		String numberString = "";
		
		numberString += nums[(int) (Math.random() * 9)];
		
		for (int i = 1; i < 9; i++) {
			numberString += nums[(int) (Math.random() * 10)];
		}
		
		String id = "admin";
		String password = passwordEncoder.encode("a");
		int no = Integer.parseInt(numberString);
		
		Admin admin = Admin.builder().id(id)
				.password(password)
				.no(no)
				.name("admin")
				.ssn("123456-7890123")
				.email("admin@gmail.com")
				.build();
		
		adminRepository.save(admin);
		
		Member member = Member.builder().id(id)
				.password(password)
				.build();
		
		member.addRole(MemberRole.ADMIN);
		
		memberRepository.save(member);
	}
	
	// member_role_set 테이블 수정
	@Test
	public void addSuperAdmin() {
		String id = "s.admin";
		String password = passwordEncoder.encode("a");
		int no = 1;
		
		Admin admin = Admin.builder().id(id)
									 .password(password)
									 .no(no)
									 .name("superAdmin")
									 .ssn("777777-7777777")
									 .phone("010-1111-1111")
									 .email("superAdmin@gmail.com")
									 .detailedAddress("서울시 용산구 이태원로 372-5")
									 .build();
		
		adminRepository.save(admin);
		
		Member member = Member.builder().id(id)
										.password(password)
										.name("superAdmin")
										.ssn("777777-7777777")
										.phone("010-1111-1111")
										.email("superAdmin@gmail.com")
										.detailedAddress("서울시 용산구 이태원로 372-5")
										.annualSalary(0)
										.build();
		
		member.addRole(MemberRole.SUPER_ADMIN);
		// member.addRole(MemberRole.ADMIN);
		
		memberRepository.save(member);
		
	}
}