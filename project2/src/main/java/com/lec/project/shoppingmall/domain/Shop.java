package com.lec.project.shoppingmall.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shop {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sno;
	
	@Column(nullable = false)
	private String gCode;
	
	@Column(nullable = false)
	private String gName;
	
	@Column(nullable = false)
	private int gPrice;
	
	@Column(nullable = false)
	private String gCategory;
	
	@Column(nullable = false)
	private int gStock;
}
