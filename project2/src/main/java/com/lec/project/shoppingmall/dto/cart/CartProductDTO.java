package com.lec.project.shoppingmall.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartProductDTO {

	private Long id;
	
	private int totalPrice;
	
	private int count;
}
