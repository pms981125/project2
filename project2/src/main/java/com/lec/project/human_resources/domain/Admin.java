package com.lec.project.human_resources.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
	@Id
	private String id;
	
	@Column(nullable = false)
	private String password;
	
	private int no;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String ssn;
	
	@Column(nullable = false)
	private String email;
}