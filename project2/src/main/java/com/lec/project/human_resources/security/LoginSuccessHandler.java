package com.lec.project.human_resources.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.lec.project.MemberSecurityDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		String id = authentication.getName(); // id
				
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"))) {
			response.sendRedirect("/sudo/goAttendance?id=" + id);
			// response.sendRedirect("/sudo/allUserList");
			// response.sendRedirect("/index.html");
		} else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			// MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
			
			// response.sendRedirect("/hr/info?id=" + memberSecurityDTO.getId());
			response.sendRedirect("/hr/userList"); // templates 폴더에 있는 html 파일에 접근 시, controller 이용
		} else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
			response.sendRedirect("/sudo/goAttendance?id=" + id);
			// response.sendRedirect("/shop/list");
		} else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
			response.sendRedirect("/index.html"); // static 폴더에 있는 html 파일로 접근 시
			//response.sendRedirect("/user/home");
		} else {
            // 기본 권한이 없는 경우 USER 권한으로 간주하고 index.html로 리다이렉트
            response.sendRedirect("/index.html");
        }
	}
}