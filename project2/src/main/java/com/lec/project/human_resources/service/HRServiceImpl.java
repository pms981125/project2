package com.lec.project.human_resources.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
	
	@Override
	public AdminDTO getAdmin(String id) {
		Optional<Admin> result = adminRepository.findById(id);
		Admin admin = result.orElseThrow();
		AdminDTO adminDTO = modelMapper.map(admin, AdminDTO.class);
		
		return adminDTO;
	}
	
	@Override
	public MemberSecurityDTO getUser(String id) {
		Optional<Member> result = memberRepository.findById(id);
		Member member = result.orElseThrow();
		MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(member.getId(),
																	member.getPassword(),
																	member.getRoleSet()
																		  .stream()
																		  .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toList()));
		
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
}