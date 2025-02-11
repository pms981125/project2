package com.lec.project.human_resources.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {
	private String id;
	
	private int no;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String ssn;
	
	@NotEmpty
	private String email;
	
	
}