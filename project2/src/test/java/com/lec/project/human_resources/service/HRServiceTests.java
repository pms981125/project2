package com.lec.project.human_resources.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lec.project.MemberSecurityDTO;
import com.lec.project.human_resources.repository.AdminRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class HRServiceTests {
	@Autowired
	AdminRepository adminRepository;
	
	@Autowired
	HRService hrService;
	
	@Test
	public void testGetUserList() {
		List<MemberSecurityDTO> memberSecurityDTOList = new ArrayList<>();
		
		memberSecurityDTOList = hrService.getUserList();
		
		log.info(memberSecurityDTOList);
	}
}