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
	private String productCode;
	
	@NotEmpty
	private String productName;
	
	@Min(0)
	private int productPrice;
	
	private String productCategory;
	
	@Min(0)
	private int productStock;
	
	private String firstProductDetail;
	
	private String secondProductDetail;
}
