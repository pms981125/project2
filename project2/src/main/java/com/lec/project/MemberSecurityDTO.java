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
	private String name;
	private String ssn;
	private String phone;
	private String email;
	private String address;
	private int annualSalary;
	private int totalSpent;
	private MemberRank memberRank;
	
	public MemberSecurityDTO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		
		this.id = username;
		this.password = password;
	}
	
	public MemberSecurityDTO(String username, String password, Collection<? extends GrantedAuthority> authorities,
							 String name, String ssn, String phone, String email, String address, int annualSalary, int totalSpent) {
		super(username, password, authorities);
		this.id = username;
		this.password = password;
		this.name = name;
		this.ssn = ssn;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.annualSalary = annualSalary;
		this.totalSpent = totalSpent;

		if (this.totalSpent >= 2000000) {
			this.memberRank = MemberRank.VVIP;
		} else if (this.totalSpent >= 1000000) {
			this.memberRank = MemberRank.VIP;
		} else {
			this.memberRank = MemberRank.NORMAL;
		}
	}
	
	public boolean isAdmin() {
		return this.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
}