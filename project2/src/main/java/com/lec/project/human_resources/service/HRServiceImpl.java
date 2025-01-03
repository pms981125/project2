package com.lec.project.human_resources.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

		MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(member.getId(), member.getPassword(),
				member.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
						.collect(Collectors.toList()));

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

			if (!roleSet.contains(MemberRole.ADMIN) && !roleSet.contains(MemberRole.SUPER_ADMIN)) {
				MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(member.getId(), member.getPassword(),
						member.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
								.collect(Collectors.toList()));
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
			MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(member.getId(), member.getPassword(),
					member.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
							.collect(Collectors.toList()));
			memberSecurityDTOList.add(memberSecurityDTO);
		}

		log.info(memberSecurityDTOList);

		return memberSecurityDTOList;
	}

	@Override
	public void update(String originalId, String newId) {
		log.info(originalId + " fvf " + newId);

		Optional<Member> result = memberRepository.findById(originalId);
		Member member = result.orElseThrow();

		// member.setId(newId); // 기본키는 수정하지 않는다.
		// member.setPassword(password); // password 수정, 암호화 도입으로 수행 X

		memberRepository.save(member);
	}

	/*
		private boolean isUniqueId(String newId) { 사용 X
			try {
				Optional<Member> result = memberRepository.findById(newId);
				Member member = result.orElseThrow();
				
				return false;
			} catch (Exception e) {
				return true;
			}
		}
	*/
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

		Admin admin = Admin.builder().id(id).password(password).name(name).no(no).ssn(ssn).email(email).build();

		adminRepository.save(admin);

		Member member = Member.builder().id(id).password(password).build();

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

	@Override
	public Page<MemberSecurityDTO> getUserListWithPaging(Pageable pageable, int size) {
		int page = pageable.getPageNumber() - 1;
		int limit = size;

		Page<Member> pages = memberRepository.findAll(PageRequest.of(page, limit));
		Page<MemberSecurityDTO> DTOPages = new PageImpl<>(
				pages.getContent().stream().filter(member -> member.getRoleSet().contains(MemberRole.USER))
						.map(member -> new MemberSecurityDTO(member.getId(), member.getPassword(),
								member.getRoleSet().stream()
										.map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
										.collect(Collectors.toList())))
						.collect(Collectors.toList()),
				pages.getPageable(), // 기존 Pageable 유지
				pages.getTotalElements() // 원래의 총 요소 개수
		);

		return DTOPages;
	}

	@Override
	public Page<MemberSecurityDTO> getAllUserListWithPaging(Pageable pageable, int size) {
		int page = pageable.getPageNumber() - 1;
		int limit = size;

		Page<Member> pages = memberRepository.findAll(PageRequest.of(page, limit));
		Page<MemberSecurityDTO> DTOPages = pages
				.map(p -> new MemberSecurityDTO(p.getId(), p.getPassword(), p.getRoleSet().stream()
						.map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toList())));
		
		return DTOPages;
	}

	@Override
	public Page<MemberSecurityDTO> getAdminListWithPaging(int p, Pageable pageable, int size) {
		int page = pageable.getPageNumber() - 1;
		int limit = size;
		
		List<Member> listSize = memberRepository.findAll().stream().filter(member -> member.getRoleSet().contains(MemberRole.ADMIN)).collect(Collectors.toList());
		Page<Member> pages = memberRepository.findAll(PageRequest.of(page, limit));
		List<MemberSecurityDTO> DTOPages = pages.getContent().stream()
												.filter(member -> member.getRoleSet().contains(MemberRole.ADMIN))
												.map(member -> new MemberSecurityDTO(
													 member.getId(),
													 member.getPassword(),
													 member.getRoleSet().stream()
													 	   .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
													 	   .collect(Collectors.toList())))
												.collect(Collectors.toList());
		
		return new PageImpl<>(DTOPages, pageable.withPage(p), listSize.size());
	}

	@Override
	public void initializePassword(String memberId) {
		Optional<Member> result = memberRepository.findById(memberId);
		Member member = result.orElseThrow();
		
		// String password = passwordEncoder.encode(generatePassword());
	}
}