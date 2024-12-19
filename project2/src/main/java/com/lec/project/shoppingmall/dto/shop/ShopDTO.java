package com.lec.project.shoppingmall.dto.shop;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopDTO {

	
	private Long bno;
	
	@NotEmpty//게시판에 들어갈 상품코드
	private String board_code;
	
	@NotEmpty//게시판에 들어갈 상품이름
	private String board_title;
	
	@Min(0)//게시판에 들어갈 상품가격
	private int board_price;
	
	//상품카테고리
	private String board_category;
	
	@Min(0)//상품재고
	private int board_stock;
	
	private String board_content1;
	
	private String board_content2;
}
