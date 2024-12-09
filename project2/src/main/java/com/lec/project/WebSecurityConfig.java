package com.lec.project;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

public class WebSecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests.anyRequest().permitAll()).formLogin((formLogin) -> formLogin.usernameParameter("username")
																																		.passwordParameter("password")
																																		.defaultSuccessUrl("/", true));
        http.csrf(csrf -> csrf.disable());
		
		return http.build();
	}

	
}