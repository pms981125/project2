package com.lec.project.human_resources.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lec.project.Member;
import com.lec.project.MemberRepository;
import com.lec.project.MemberRole;
import com.lec.project.MemberSecurityDTO;
import com.lec.project.human_resources.domain.Admin;
import com.lec.project.human_resources.dto.AdminDTO;
import com.lec.project.human_resources.repository.AdminRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class HRServiceImpl implements HRService {
	private final MemberRepository memberRepository;
	private final AdminRepository adminRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	
	/*	@Override
		public AdminDTO getAdmin(String id) {
			Optional<Admin> result = adminRepository.findById(id);
			Admin admin = result.orElseThrow();
			AdminDTO adminDTO = modelMapper.map(admin, AdminDTO.class);
			
			return adminDTO;
		}*/
	
	@Override
	public MemberSecurityDTO getUser(String id) {
		Optional<Member> result = memberRepository.findById(id);
		Member member = result.orElseThrow();
		
		// 패스워드 복호화? 
		
		MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(member.getId(),
																	member.getPassword(),
																	member.getRoleSet()
																		  .stream()
																		  .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toList()));
		
		log.info(memberSecurityDTO);
		
		return memberSecurityDTO;
	}

	@Override
	public List<MemberSecurityDTO> getUserList() {
		List<Member> memberList = memberRepository.findAll();
		List<MemberSecurityDTO> memberSecurityDTOList = new ArrayList<>();
		Set<MemberRole> roleSet = new HashSet<>();
		
		for (Member member : memberList) {
			roleSet = member.getRoleSet();
			
			if (!roleSet.contains(MemberRole.ADMIN)) {
				MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(member.getId(),
																			member.getPassword(),
																			member.getRoleSet()
																				  .stream()
																				  .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toList()));
				memberSecurityDTOList.add(memberSecurityDTO);
			}
		}
		
		return memberSecurityDTOList;
	}
	
	@Override
	public List<MemberSecurityDTO> getAllUserList() {
		List<Member> memberList = memberRepository.findAll();
		List<MemberSecurityDTO> memberSecurityDTOList = new ArrayList<>();
		
		for (Member member : memberList) {
			MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(member.getId(),
																		member.getPassword(),
																		member.getRoleSet()
																			  .stream()
																			  .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toList()));
			memberSecurityDTOList.add(memberSecurityDTO);
		}

		log.info(memberSecurityDTOList);
		
		return memberSecurityDTOList;
	}

	@Override
	public void update(String id, String password) {
		Optional<Member> result = memberRepository.findById(id);
		Member member = result.orElseThrow();
		
		member.setPassword(password); // password 수정
		memberRepository.save(member);
	}

	@Override
	public void remove(String id) {
		memberRepository.deleteById(id);
	}

	@Override
	public void addAdmin(String id, String password, String name, String ssnFront, String ssnEnd, String email) {
		String[] nums = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
		String numberString = "";
		
		numberString += nums[(int) (Math.random() * 9)];
		
		for (int i = 1; i < 9; i++) {
			numberString += nums[(int) (Math.random() * 10)];
		}

		int no = Integer.parseInt(numberString);
		String ssn = ssnFront + "-" + ssnEnd; 
		
		password = passwordEncoder.encode(password);

		Admin admin = Admin.builder().id(id)
									 .password(password)
									 .name(name)
									 .no(no)
									 .ssn(ssn)
									 .email(email)
									 .build();
		
		adminRepository.save(admin);
		
		Member member = Member.builder().id(id)
										.password(password)
										.build();
		
		member.addRole(MemberRole.ADMIN);
		memberRepository.save(member);
	}

	@Override
	public void delagateAuthority(String superAdminId, String adminId) {
		Optional<Member> result = memberRepository.findById(superAdminId);
		Member member = result.orElseThrow();
		
		member.removeRole(MemberRole.SUPER_ADMIN);
		memberRepository.save(member);
		
		result = memberRepository.findById(adminId);
		member = result.orElseThrow();
		
		member.addRole(MemberRole.SUPER_ADMIN);
		memberRepository.save(member);
	}
}