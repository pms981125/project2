package com.lec.project.human_resources.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lec.project.MemberSecurityDTO;
import com.lec.project.human_resources.dto.AdminDTO;

public interface HRService {
	MemberSecurityDTO getUser(String id);
	//AdminDTO getAdmin(String id);
	List<MemberSecurityDTO> getUserList();
	Page<MemberSecurityDTO> getUserListWithPaging(Pageable pageable);
	List<MemberSecurityDTO> getAllUserList();
	Page<MemberSecurityDTO> getAllUserListWithPaging(Pageable pageable);
	void update(String id, String password);
	void remove(String id);
	void addAdmin(String id, String password, String name, String ssnFront, String ssnEnd, String email);
	void delagateAuthority(String superAdminId, String adminId);
}