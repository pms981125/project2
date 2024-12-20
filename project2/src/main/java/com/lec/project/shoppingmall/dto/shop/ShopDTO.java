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
}
