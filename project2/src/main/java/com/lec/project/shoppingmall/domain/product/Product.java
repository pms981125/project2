package com.lec.project.shoppingmall.domain.product;

import java.util.ArrayList;
import java.util.List;

import com.lec.project.shoppingmall.domain.product.productimage.ProductImage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	
	@Builder.Default
	@OneToMany(mappedBy = "product")
	private List<ProductImage> productImages = new ArrayList<>();
}
