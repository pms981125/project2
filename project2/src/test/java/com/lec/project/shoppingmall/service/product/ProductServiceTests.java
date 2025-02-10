package com.lec.project.shoppingmall.service.product;

import java.util.stream.IntStream;

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
								.productCode("product_code Test.....")
								.productCategory("product_category Test.....")
								.firstProductDetail("1st...detail")
								.secondProductDetail("2nd...detail")
								.productName("product_name Test.....")
								.productPrice(77777)
								.productStock(123)
								.build();
		
		String productName = productService.register(productDTO);
		
		log.info("상품이름 = " + productName);
	}
	
	@Test
	@DisplayName("상품 여러건 등록하기")
	public void testInsertMany() {
		
		IntStream.rangeClosed(1, 1000).forEach(i -> {
			
			ProductDTO productDTO = ProductDTO.builder()
					.productCode(String.format("상품코드 %03d", i))
					.productCategory(String.format("category %03d", i%5))
					.firstProductDetail(String.format("세부정보 %03d", i))
					.secondProductDetail(String.format("세부정보 %03d", i))
					.productName(String.format("상품 %03d", i))
					.productPrice(i*100)
					.productStock(100)
					.build();
			
			String product_name = productService.register(productDTO);
			
			log.info("상품이름 = " + product_name);
		});
	}
	
}
