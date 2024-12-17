package com.lec.project.human_resources.service;

import java.util.List;

import com.lec.project.MemberSecurityDTO;
import com.lec.project.human_resources.dto.AdminDTO;

public interface HRService {
	AdminDTO getAdmin(String id);
	MemberSecurityDTO getUser(String id);
	List<MemberSecurityDTO> getUserList();
}