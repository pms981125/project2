package com.lec.project.shoppingmall.domain.shop;

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
	private Long bno;
	
	@Column
	private String board_code;
	
	@Column(nullable = false)
	private String board_title;
	
	@Column(nullable = false)
	private int board_price;
	
	@Column
	private String board_category;
	
	@Column(nullable = false)
	private int board_stock;
	
	@Column
	private String board_content1;
	
	@Column
	private String board_content2;
	
	public void changeCode(String board_code) {
		this.board_code = board_code;
	}
}
