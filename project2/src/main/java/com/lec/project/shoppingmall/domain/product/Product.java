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
	private String productCode;
	
	@Column(nullable = false)
	private String productName;
	
	@Column
	private int productPrice;
	
	@Column
	private String productCategory;
	
	@Column
	private int productStock;
	
	@Column
	private String firstProductDetail;
	
	@Column
	private String secondProductDetail;
}
