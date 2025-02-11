package com.lec.project.human_resources.service;

import java.time.LocalTime;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lec.project.MemberSecurityDTO;
import com.lec.project.human_resources.dto.AdminDTO;
import com.lec.project.human_resources.dto.CalendarDTO;

public interface HRService {
	MemberSecurityDTO getUser(String id);
	//AdminDTO getAdmin(String id);
	List<MemberSecurityDTO> getUserList();
	Page<MemberSecurityDTO> getUserListWithPaging(Pageable pageable, int size);
	List<MemberSecurityDTO> getAllUserList();
	Page<MemberSecurityDTO> getAllUserListWithPaging(Pageable pageable, int size);
	Page<MemberSecurityDTO> getAdminListWithPaging(int p, Pageable pageable, int size);
	void update(String id, String name, String ssn, String phone, String email, String address, int annualSalary);
	void remove(String id, boolean isAdmin);
	void addAdmin(String id, String password, String name, String ssn, String phone, String email, String address, int salary, String job);
	void delagateAuthority(String superAdminId, String adminId);
	void initializePassword(String memberId) throws AddressException, MessagingException;
	void addMember(String id, String password, String name, String ssn, String phone, String email, String address, String location);
	void exaltation(String id, int salary);
	boolean confirmId(String id);
	void attendance(String id, LocalTime localTime);
	void leave(String id, LocalTime localTime);
	CalendarDTO[] getWorkLog(String id);
}