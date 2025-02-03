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
	
	private String productName;
	
	private String productCode;
	
	private int count;
	
	private int price;
	
	private int totalPrice;
	
	private String imgPath;
	
	private String thumbnailPath;
}
