package com.lec.project;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lec.project.human_resources.domain.Admin;
import com.lec.project.human_resources.repository.AdminRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class MemberRepositoryTests {
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Test
	public void addUser() {
		Member member = Member.builder().id("user01")
							  .password(passwordEncoder.encode("u"))
							  .name("user01")
							  .ssn("123456-1234567")
							  .phone("010-1234-4321")
							  .email("user@gmail.com")
							  .detailedAddress("서울시 서초구 만이빌딩 5층 505호")
							  .annualSalary(0)
							  .build();
		
		member.addRole(MemberRole.USER);
		
		memberRepository.save(member);
	}
	
	@Test
	public void addManager() {
		Member member = Member.builder().id("manager")
							  .password(passwordEncoder.encode("m"))
							  .name("sungil")
							  .ssn("222222-1222222")
							  .phone("010-2222-3333")
							  .email("sungil@gmail.com")
							  .detailedAddress("서울시 강남구 125-126")
							  .annualSalary(30000000)
							  .build();
		
		member.addRole(MemberRole.MANAGER);
		
		memberRepository.save(member);
		
		Admin admin = Admin.builder().id("manager")
				  		   .password(passwordEncoder.encode("m"))
				  		   .name("sungil")
				  		   .ssn("222222-1222222")
				  		   .phone("010-2222-3333")
				  		   .email("sungil@gmail.com")
				  		   .detailedAddress("서울시 강남구 125-126")
				  		   .annualSalary(30000000)
				  		   .build();
		
		adminRepository.save(admin);
	}
	
	@Test
	public void addUser30() {
		String[] detailedAddress = { "서울시 서초구 만이빌딩 5층 505호", "인천시 미추홀구 301-2", "부산시 서면구 123-78", "제주시 서귀포구 76-93" };
		String[] names = { "옥성일", "송현일", "박민성", "정다운" };
		String[] region = { "Seoul", "Busan", "Incheon", "Jeju" };
		
		IntStream.rangeClosed(1, 30).forEach(i -> {
			Member member = Member.builder().id(String.format("user%02d", i))
											.password(passwordEncoder.encode("u"))
											.name(names[i % 4])
											.ssn(String.format("123456-12345%02d", i))
											.phone(String.format("010-1234-43%02d", i))
											.email(String.format("user%02d@gmail.com", i))
											.detailedAddress(detailedAddress[i % 4])
											.annualSalary(0)
											.build();
			
			member.addRole(MemberRole.USER);
			
			memberRepository.save(member);
			
		});
	}
	
	@Test
	public void addA30() {
		IntStream.rangeClosed(1, 30).forEach(i -> {
			Member member = Member.builder().id(String.format("admin%03d", i * 10))
					.password(passwordEncoder.encode("a"))
					.build();
			member.addRole(MemberRole.ADMIN);
			
			memberRepository.save(member);
		});
	}
}