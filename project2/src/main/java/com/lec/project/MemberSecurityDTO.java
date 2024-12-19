package com.lec.project;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User{
	private String id;
	private String password;
	
	public MemberSecurityDTO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		
		this.id = username;
		this.password = password;
	}
	
	public boolean isAdmin() {
		return this.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
}