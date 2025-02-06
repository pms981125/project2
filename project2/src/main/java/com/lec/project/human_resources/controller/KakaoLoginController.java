package com.lec.project.human_resources.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.project.Member;
import com.lec.project.MemberRepository;
import com.lec.project.MemberRole;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class KakaoLoginController {
	 private final MemberRepository memberRepository;
	    private final PasswordEncoder passwordEncoder;

	    @GetMapping("/login/oauth2/code/kakao")
	    public String kakaoCallback(@RequestParam String code, HttpSession session) {
	        try {
	            // 1. 토큰 받기
	            RestTemplate rt = new RestTemplate();
	            HttpHeaders headers = new HttpHeaders();
	            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

	            String tokenUrl = "https://kauth.kakao.com/oauth/token"
	                    + "?grant_type=authorization_code"
	                    + "&client_id=ab8323fae2f0119772ecb3325e49c2f1"
	                    + "&redirect_uri=http://localhost:8090/login/oauth2/code/kakao"
	                    + "&code=" + code;

	            ResponseEntity<String> tokenResponse = rt.exchange(
	                tokenUrl,
	                HttpMethod.POST,
	                new HttpEntity<>(headers),
	                String.class
	            );

	            // 2. 토큰으로 사용자 정보 받기
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode tokenJson = objectMapper.readTree(tokenResponse.getBody());
	            String accessToken = tokenJson.get("access_token").asText();

	            headers = new HttpHeaders();
	            headers.add("Authorization", "Bearer " + accessToken);
	            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

	            ResponseEntity<String> profileResponse = rt.exchange(
	                "https://kapi.kakao.com/v2/user/me",
	                HttpMethod.POST,
	                new HttpEntity<>(headers),
	                String.class
	            );

	            // 3. 사용자 정보 파싱
	            JsonNode userInfo = objectMapper.readTree(profileResponse.getBody());
	            String email = userInfo.get("kakao_account").get("email").asText();
	            String nickname = userInfo.get("properties").get("nickname").asText();

	            // 4. 회원가입 또는 로그인
	            Member member = memberRepository.findByEmail(email)
	                .orElseGet(() -> {
	                    Member newMember = Member.builder()
	                        .id(email)
	                        .email(email)
	                        .password(passwordEncoder.encode("kakao" + userInfo.get("id").asText()))
	                        .name(nickname)
	                        .build();
	                    newMember.addRole(MemberRole.USER);
	                    return memberRepository.save(newMember);
	                });

	            session.setAttribute("user", member);
	            return "redirect:/";

	        } catch (Exception e) {
	            return "redirect:/user/login?error=true";
	        }
	    }
}
