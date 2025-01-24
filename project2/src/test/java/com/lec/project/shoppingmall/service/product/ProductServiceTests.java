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
	
	@Test
	@DisplayName("상품 여러건 등록하기")
	public void testInsertMany() {
		
		IntStream.rangeClosed(1, 1000).forEach(i -> {
			
			ProductDTO productDTO = ProductDTO.builder()
					.product_code(String.format("상품코드 %03d", i))
					.product_category(String.format("category %03d", i%5))
					.product_detail1(String.format("세부정보 %03d", i))
					.product_detail2(String.format("20일(현지시간) 트럼프 대통령은 취임사에서 AI 및 바이오 헬스케어 등 첨단산업과 관련된 구체적인 언급을 하지는 않았다. 이날 꺼내든 첫 번째 행정명령 카드는 ‘이민자 추방 정책’과 ‘무역 시스템의 개편’이었다. %03d", i))
					.product_name(String.format("상품 %03d", i))
					.product_price(i*100)
					.product_stock(i)
					.build();
			
			String product_name = productService.register(productDTO);
			
			log.info("상품이름 = " + product_name);
		});
	}
	
}
