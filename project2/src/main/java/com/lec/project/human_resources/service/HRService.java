package com.lec.project.human_resources.service;

import java.util.List;

import com.lec.project.MemberSecurityDTO;
import com.lec.project.human_resources.dto.AdminDTO;

public interface HRService {
	MemberSecurityDTO getUser(String id);
	AdminDTO getAdmin(String id);
	List<MemberSecurityDTO> getUserList();
	//void update
}