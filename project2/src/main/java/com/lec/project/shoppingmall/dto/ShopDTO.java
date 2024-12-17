package com.lec.project.shoppingmall.dto;

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

	
	private Long sno;
	//상품코드
	private String gCode;
	
	@NotEmpty//상품이름
	private String gName;
	
	@NotEmpty//상품가격
	@Size(min = 0)
	private int gPrice;
	
	@NotEmpty//상품카테고리
	private String gCategory;
	
	@NotEmpty//상품재고
	@Size(min = 0)
	private int gStock;
}
