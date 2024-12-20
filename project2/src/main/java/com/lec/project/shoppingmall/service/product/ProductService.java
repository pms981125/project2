package com.lec.project.shoppingmall.service.product;

import com.lec.project.shoppingmall.domain.product.Product;
import com.lec.project.shoppingmall.dto.product.ProductDTO;

public interface ProductService {

	String register(ProductDTO productDTO);
	Product findById(String productCode);
}

