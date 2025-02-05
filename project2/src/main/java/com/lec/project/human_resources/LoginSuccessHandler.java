package com.lec.project.human_resources;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		log.info(authentication.getAuthorities() + "-=-=-=-=-=-=-=-=-=-");	
	
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"))) {
			response.sendRedirect("/sudo/allUserList");
			// response.sendRedirect("/index.html");
		} else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			// MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
			
			// response.sendRedirect("/hr/info?id=" + memberSecurityDTO.getId());
			response.sendRedirect("/hr/userList"); // templates 폴더에 있는 html 파일에 접근 시, controller 이용
		} else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
			response.sendRedirect("/shop/list");
		} else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
			response.sendRedirect("/index.html"); // static 폴더에 있는 html 파일로 접근 시
			//response.sendRedirect("/user/home");
		} else {
			response.sendRedirect("/contact");
		}
	}
}