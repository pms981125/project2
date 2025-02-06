package com.lec.project.human_resources.service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.lec.project.Member;
import com.lec.project.MemberRepository;
import com.lec.project.MemberRole;
import com.lec.project.MemberSecurityDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService{
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		log.info("==========> User Request : " + userRequest);		
		log.info("OAuth2 User .............................");
		
		ClientRegistration clientRegistration = userRequest.getClientRegistration();
		String clientName = clientRegistration.getClientName();		
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		Map<String, Object> paramMap = oAuth2User.getAttributes();		

		
		String email = null;
		switch(clientName) {
		case "kakao": 
			email = getKakoEmail(paramMap);
			break;
		}	
		
		if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }
				
		return generateDTO(email, paramMap);
		
	}
	
	private OAuth2User generateDTO(String email, Map<String, Object> paramMap) {
		Optional<Member> result = memberRepository.findByEmail(email);
		
		// 데이터베이스에 해당 이메일사용자가 없다면
		// 이미 가입한 회원은 기존정보를 리턴하고 새롭게 SSN로그인된 회원은
		// 자동으로 회원가입처리
		if(result.isEmpty()) {
			// 회원추가 : mid=이메일, mpw=12345
			Member member = Member.builder()
					.id(email)
					.password(passwordEncoder.encode("12345"))
					.email(email)
					.build();
			member.addRole(MemberRole.USER);
			memberRepository.save(member);
			
			MemberSecurityDTO memberSecurityDTO = 
				new MemberSecurityDTO(email, "12345",
						Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
			memberSecurityDTO.setProps(paramMap);
			
			return memberSecurityDTO;
		} else {
			// SSN로그인한 회원과 일반가입 회원구분처리
			Member member = result.get();
			MemberSecurityDTO memberSecurityDTO
				= new MemberSecurityDTO(
						member.getId(), 
						member.getPassword(), 
						member.getRoleSet()
						      .stream()
						      .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
						      .collect(Collectors.toList()));

			return memberSecurityDTO;
		}
	}

	private String getKakoEmail(Map<String, Object> paramMap) {
		log.info("KAKAO ---------------------------------------------");
		
		Object value = paramMap.get("kakao_account");
		log.info(value);
		
		LinkedHashMap accountMap = (LinkedHashMap) value;
		String email = (String) accountMap.get("email");
		log.info("카카오 email = " + email);
		
		// kakao에서 동의항목에 email옵션이 활성화 되어 있지 않기 때문에 강제로 주입
		// return email;
		return email;
	}
}
