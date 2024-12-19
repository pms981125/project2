package com.lec.project.shoppingmall.service;

import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lec.project.shoppingmall.domain.shop.Shop;
import com.lec.project.shoppingmall.dto.shop.ShopDTO;
import com.lec.project.shoppingmall.repository.ShopRepository;
import com.lec.project.shoppingmall.service.shop.ShopService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class ShopServiceTests {

	@Autowired
	ShopRepository shopRepository;
	
	@Autowired
	ShopService shopService;
	
	@Test
	@DisplayName("상품 등록하기.....")
	public void testInsert() {
		log.info("......" + shopService.getClass().getName());
		
		ShopDTO shopDTO = ShopDTO.builder()
								.board_code("board_code Test.....")
								.board_title("product_name Test.....")
								.board_price(10000)//goods_price(productDTO.getProduct_price)
								.board_category("product_category Test.....")
								.board_stock(1000)
								.board_content1("product_detail 1번입니다.")
								.board_content2("product_detail 번입니다.")
								.build();
		
		Long bno = shopService.register(shopDTO);
		
		log.info("게시글번호 =" + bno);
	}
	
	@Test
	@DisplayName("게시판 여러건 등록하기")
	public void testInsertMany() {
		
		IntStream.rangeClosed(1, 100).forEach(i -> {
			
			Shop shop = Shop.builder()
							.board_code(String.format("%3d board_code", i))
							.board_title(String.format("%3d product_name", i))
							.board_price(i*1000)
							.board_category(String.format("%3d product_category", i))
							.board_stock(i)
							.board_content1(null)
							.board_content2(null)
							.build();
			
			Shop result = shopRepository.save(shop);
			
			log.info("게시판 번호 = " + result.getBno());
		});
	}
	
	@Test
	@DisplayName("게시글 조회하기")
	public void testSelect() {
		
		Long bno = 50L;
		Optional<Shop> result = shopRepository.findById(bno);
		Shop shop = result.orElseThrow();
		log.info(shop);
	}
	
	@Test
	@DisplayName("상품 게시글 삭제하기.....")
	public void testDelete() {
		
		Long bno = 1L;
		Optional<Shop> result = shopRepository.findById(bno);
		Shop shop = result.orElseThrow();
		shopRepository.deleteById(bno);
		log.info(shop);
	}
	
	@Test
	@DisplayName("상품 수정하기")
	public void testUpdate() {
		Long bno = 1L;
		Optional<Shop> result = shopRepository.findById(bno);
		Shop shop = result.orElseThrow();
		shop.changeCode("[수정완료]" + shop.getBoard_code());
		shopRepository.save(shop);
		log.info(shop);
	}
	
}
