package com.lec.project;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class MemberRepositoryTests {
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Test
	public void addUser() {
		Member member = Member.builder().id("user")
				.password(passwordEncoder.encode("u"))
				.build();
		member.addRole(MemberRole.USER);
		
		memberRepository.save(member);
	}
	
	@Test
	public void addManager() {
		Member member = Member.builder().id("manager")
				.password(passwordEncoder.encode("m"))
				.build();
		member.addRole(MemberRole.MANAGER);
		
		memberRepository.save(member);
	}
	
	@Test
	public void addUser30() {
		IntStream.rangeClosed(1, 30).forEach(i -> {
			Member member = Member.builder().id(String.format("user%02d", i))
					.password(passwordEncoder.encode("u"))
					.build();
			member.addRole(MemberRole.USER);
			
			memberRepository.save(member);
		});
	}
}