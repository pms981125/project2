package com.lec.project;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
	protected CustomAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
	    String username = request.getParameter("username");
	    String password = request.getParameter("password");
	    
	    username = (username != null) ? username.trim() : "";
	    password = (password != null) ? password : "";
	    
	    UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
	    // Allow subclasses to set the "details" property
	    // setDetails(request, authRequest);
	    
	    return this.getAuthenticationManager().authenticate(authRequest);
	}
}