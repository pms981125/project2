package com.lec.project;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserDetailsServiceCustom implements UserDetailsService {
	private final MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Member> result = memberRepository.findByIdWithRoles(username);
		
		if (result.isEmpty()) {
			throw new UsernameNotFoundException("해당되는 ID의 유저가 없습니다.");
		}
		
		Member member = result.orElseThrow();
		// Collection<GrantedAuthority> collection = Collections.singleton(new SimpleGrantedAuthority("test"));
		// Collection<GrantedAuthority> collection = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + member.getId().toUpperCase()));
		// MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(member.getId(), "{noop}" + member.getPassword(), collection);
		MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(member.getId(),
																	member.getPassword(),
																	member.getRoleSet()
																		  .stream()
																		  .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toList()));
		
		return memberSecurityDTO;
	}
}