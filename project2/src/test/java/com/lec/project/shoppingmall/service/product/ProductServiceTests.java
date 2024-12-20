package com.lec.project.shoppingmall.service.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lec.project.shoppingmall.dto.product.ProductDTO;
import com.lec.project.shoppingmall.repository.ProductRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class ProductServiceTests {

	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ProductService productService;
	
	@Test
	@DisplayName("상품 등록하기.....")
	public void testInsert() {
		log.info("....." + productService.getClass().getName());
		
		ProductDTO productDTO = ProductDTO.builder()
								.product_code("product_code Test.....")
								.product_category("product_category Test.....")
								.product_detail1("1st...detail")
								.product_detail2("2nd...detail")
								.product_name("product_name Test.....")
								.product_price(77777)
								.product_stock(123)
								.build();
		
		String product_name = productService.register(productDTO);
		
		log.info("상품이름 = " + product_name);
	}
	
}
