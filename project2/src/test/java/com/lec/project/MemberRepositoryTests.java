package com.lec.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class MemberRepositoryTests {
	@Autowired
	private MemberRepository memberRepository;
	
	@Test
	public void addUser() {
		Member member = Member.builder().id("user1")
				.password("u")
				.build();
		member.addRole(MemberRole.USER);
		
		memberRepository.save(member);
	}
}