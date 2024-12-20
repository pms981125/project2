package com.lec.project.shoppingmall.dto.product;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

	@NotEmpty
	private String product_code;
	
	@NotEmpty
	private String product_name;
	
	@Min(0)
	private int product_price;
	
	private String product_category;
	
	@Min(0)
	private int product_stock;
	
	private String product_detail1;
	
	private String product_detail2;
}
