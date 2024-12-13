package com.lec.project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.lec.project.human_resources.LoginSuccessHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
// @EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		log.info("filter1 =-=-=-==-=");
		// pull
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/", "/login").permitAll()
												.requestMatchers("/hr_main").hasAnyRole("admin")
												.requestMatchers("/index").hasAnyRole("test")
												.anyRequest().authenticated());
		/*
		http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests.anyRequest().permitAll())
																						.formLogin((formLogin) -> formLogin.usernameParameter("username")
																						.passwordParameter("password")
																						.successHandler(successHandler()));
																						// .defaultSuccessUrl("/", true));
		 */
		http.csrf(csrf -> csrf.disable());
        
		return http.build();
	}
	
	@Bean
	AuthenticationSuccessHandler successHandler() {
		return new LoginSuccessHandler();
	}
}