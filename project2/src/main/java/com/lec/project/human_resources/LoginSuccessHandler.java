package com.lec.project.human_resources;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.lec.project.MemberSecurityDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		log.info(authentication.getAuthorities() + "-=-=-=-=-=-=-=-=-=");	
	
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			// response.sendRedirect("/files/admin/admin.html");
			
			 // response.sendRedirect("/hr_main.html");
			MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
			
			log.info(authentication);
			// response.sendRedirect("/hr/info?id=" + memberSecurityDTO.getId()); // templates 폴더에 있는 html 파일에 접근 시 controller를 이용
			response.sendRedirect("/hr/info");
		} else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
			response.sendRedirect("/index.html"); // static 폴더에 있는 html 파일로 접근 시
		} else {
			response.sendRedirect("/contact");
		}
	}
}