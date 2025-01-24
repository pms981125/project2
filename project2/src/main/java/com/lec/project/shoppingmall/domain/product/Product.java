package com.lec.project.shoppingmall.domain.product;

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
public class Product {

	@Id
	private String product_code;
	
	@Column(nullable = false)
	private String product_name;
	
	@Column
	private int product_price;
	
	@Column
	private String product_category;
	
	@Column
	private int product_stock;
	
	@Column
	private String product_detail1;
	@Column
	private String product_detail2;
}
