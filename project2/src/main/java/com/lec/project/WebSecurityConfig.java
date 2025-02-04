package com.lec.project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.lec.project.human_resources.LoginFailureHandler;
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
        http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
        // http.csrf(csrf -> csrf.disable())
        	.authorizeHttpRequests(auth -> auth.requestMatchers("/admin/**").hasRole("ADMIN") // 효과 X
        									   .requestMatchers("/hr/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
        									   .requestMatchers("/sudo/**").hasRole("SUPER_ADMIN")
        									   
        								        // protoshop 관련 권한을 MANAGER에 부여
        								       .requestMatchers("/shop/modify/**", "/shop/remove/**", "/shop/regist/**").hasRole("MANAGER")
        								       .requestMatchers("/shop/list", "/shop/read/**", "/cart/**").permitAll()
        								        
        									   // .requestMatchers("/user/**").hasRole("USER") // 없애도 될듯?
        								       // .requestMatchers("/login.html").permitAll()
        								       .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // 정적 리소스 접근 허용
        								       .requestMatchers("user/register", "user/goRegisterForm").permitAll() //123
        									   .anyRequest().authenticated())
        	// .formLogin(form -> form.loginPage("/login.html")
        	.formLogin(form -> form.loginPage("/user/login")
        						   .loginProcessingUrl("/loginProcess")
        						   .successHandler(successHandler())
        						   .failureHandler(failureHandler())
        						   .permitAll())
        	.logout(out -> out.logoutUrl("/logout")
        					  .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // GET 요청 허용
        					  .logoutSuccessUrl("/login")  // 로그아웃 성공 후 이동할 URL
        					  .invalidateHttpSession(true) // 세션 삭제
        					  .deleteCookies("JSESSIONID") // 쿠키 삭제
							  // .logoutSuccessHandler(custom)
							  .permitAll()
        );
        
		return http.build();
	}

	//바로 위 코드에 kakaoPay csrf_token 예외 처리한 코드
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		log.info("filter1 =-=-=-==-=");
//
//        http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/kakao-pay/**"))		// 카카오페이 API는 CSRF 검사 제외
//        	.authorizeHttpRequests(auth -> auth.requestMatchers("/admin/**").hasRole("ADMIN") // 효과 X
//        									   .requestMatchers("/hr/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
//        									   .requestMatchers("/sudo/**").hasRole("SUPER_ADMIN")
//        									   
//        								        // shop 관련 권한을 MANAGER에 부여
//        								       .requestMatchers("/shop/modify/**", "/shop/remove/**", "/shop/regist/**").hasRole("MANAGER")
//        								       .requestMatchers("/shop/list", "/shop/read/**", "/cart/**").permitAll()
//        								        
//        									   // .requestMatchers("/user/**").hasRole("USER") // 없애도 될듯?
//        								       // .requestMatchers("/login.html").permitAll()
//        								       .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // 정적 리소스 접근 허용
//        								       .requestMatchers("user/register", "user/goRegisterForm").permitAll() //123
//        									   .anyRequest().authenticated())
//        	// .formLogin(form -> form.loginPage("/login.html")
//        	.formLogin(form -> form.loginPage("/user/login")
//        						   .loginProcessingUrl("/loginProcess")
//        						   .successHandler(successHandler())
//        						   .failureHandler(failureHandler())
//        						   .permitAll())
//        	.logout(out -> out.logoutUrl("/logout")
//							  // .logoutSuccessHandler(custom)
//							  .permitAll()
//        );
//        
//		return http.build();
//	}
	
	@Bean
	AuthenticationSuccessHandler successHandler() {
		return new LoginSuccessHandler();
	}
	
	@Bean
	AuthenticationFailureHandler failureHandler() {
		// return new LoginFailureHandler();
		return (request, response, exception) -> {
			String errorMessage;
			
	        if (exception instanceof BadCredentialsException) {
	            errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
	        } else if (exception instanceof InternalAuthenticationServiceException) {
	            errorMessage = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
	        } else if (exception instanceof UsernameNotFoundException) {
	            errorMessage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
	        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
	            errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
	        } else {
	            errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";
	        }
	        
            request.getSession().setAttribute("errorMessage", errorMessage);
            response.sendRedirect("/login?error");
        };
	}
}