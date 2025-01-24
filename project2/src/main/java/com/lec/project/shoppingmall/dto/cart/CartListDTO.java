package com.lec.project.shoppingmall.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartListDTO {

	private Long id;
	
	private String product_name;
	
	private String product_code;
	
	private int count;
	
	private int price;
	
	private int total_price;
	
	private String img_path;
	
	private String thumbnail_path;
}
