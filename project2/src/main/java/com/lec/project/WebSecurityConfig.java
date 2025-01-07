package com.lec.project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		log.info("filter1 =-=-=-==-=");

        http.csrf(csrf -> csrf.disable())
        	.authorizeHttpRequests(auth -> auth.requestMatchers("/admin/**").hasRole("ADMIN") // 효과 X
        									   .requestMatchers("/hr/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
        									   .requestMatchers("/sudo/**").hasRole("SUPER_ADMIN")
        									   
        								        // protoshop 관련 권한을 MANAGER에 부여
        								       .requestMatchers("/protoshop/modify/**", "/protoshop/remove/**", "/protoshop/regist/**").hasRole("MANAGER")
        								       .requestMatchers("/protoshop/list", "/protoshop/read/**", "/cart/**").permitAll()
        								        
        									   // .requestMatchers("/user/**").hasRole("USER") // 없애도 될듯?
        								       // .requestMatchers("/login.html").permitAll()
        								       .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // 정적 리소스 접근 허용
        								       .requestMatchers("/user/register", "user/goRegisterForm").permitAll()
        									   .anyRequest().authenticated())
        	.formLogin(form -> form.loginPage("/login.html")
        						   .loginProcessingUrl("/loginProcess")
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