package com.lec.project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

        http.csrf(csrf -> csrf.disable())
        	.authorizeHttpRequests(auth -> auth.requestMatchers("/admin/**").hasRole("ADMIN") // 효과 X
        									   .requestMatchers("/hr/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
        									   .requestMatchers("/sudo/**").hasRole("SUPER_ADMIN")
        									   .requestMatchers("/user/**").hasRole("USER") // 없애도 될듯?
        									   .anyRequest().authenticated())
        	.formLogin(form -> form//.loginPage("/login")
        						   .permitAll()
        						   .successHandler(successHandler()))
        	.logout(out -> out.logoutUrl("/logout")
							  // .logoutSuccessHandler(custom)
							  .permitAll()
        );
        
		return http.build();
	}
	
	@Bean
	AuthenticationSuccessHandler successHandler() {
		return new LoginSuccessHandler();
	}
}