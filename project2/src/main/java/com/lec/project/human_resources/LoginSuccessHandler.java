package com.lec.project.human_resources;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private String urlString = "";
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		handle(request, response, authentication);
	}
	
	private void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		String url = getUrlFromRole(authentication);
		
		
		if (response.isCommitted()) {
			log.info("-==-=-=- commit");
			return;
		}
		log.info("-==-=-=- commit XXX");
		
		redirectStrategy.sendRedirect(request, response, url);
	}

	private String getUrlFromRole(Authentication authentication) {
		Map<String, String> urlMap = new HashMap<>();
		
		urlMap.put("test", "/");
		urlMap.put("testId", "/");
		urlMap.put("t", "/");
		urlMap.put("admin", "/hr_main.html");
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	
		authorities.forEach(auth -> {
			urlString = auth.getAuthority();
		});
		
		log.info("urls-=-=-=-=-=" + urlString);
		
		return urlMap.get(urlString);
	}
}