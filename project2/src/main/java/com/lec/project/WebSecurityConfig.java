package com.lec.project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
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