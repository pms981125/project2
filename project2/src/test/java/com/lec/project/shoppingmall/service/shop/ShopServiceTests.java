package com.lec.project.shoppingmall.service.shop;

import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lec.project.shoppingmall.domain.product.Product;
import com.lec.project.shoppingmall.domain.shop.Shop;
import com.lec.project.shoppingmall.dto.shop.ShopDTO;
import com.lec.project.shoppingmall.repository.ProductRepository;
import com.lec.project.shoppingmall.repository.ShopRepository;
import com.lec.project.shoppingmall.service.shop.ShopService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class ShopServiceTests {

	@Autowired
	ShopRepository shopRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ShopService shopService;

	@Test
	@DisplayName("게시판 등록하기.....")
	public void testInsert() {
		log.info("......" + shopService.getClass().getName());

		Product product = Product.builder().product_code("BM001") // 실제 사용할 상품 코드
				.product_name("테스트 상품").product_price(10000).product_category("테스트").product_stock(100)
				.product_detail1("상품 상세 설명 1").product_detail2("상품 상세 설명 2").build();

		productRepository.save(product);

		ShopDTO shopDTO = ShopDTO.builder().product_code("BM001") // 위에서 생성한 product_code 사용
				.build();
		Long bno = shopService.register(shopDTO);

		log.info("게시글번호 =" + bno);
	}

	@Test
    @DisplayName("여러 게시판 일괄 등록")
    public void testInsertMany() {
        IntStream.rangeClosed(1, 500).forEach(i -> {
            ShopDTO shopDTO = ShopDTO.builder()
                .product_code(String.format("상품코드 %03d", i))
                .build();

            Long bno = shopService.register(shopDTO);
            log.info("등록된 게시판 번호: {}", bno);
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
//		Long bno = 1L;
//		Optional<Shop> result = shopRepository.findById(bno);
//		Shop shop = result.orElseThrow();
//		shop.changeCode("[수정완료]" + shop.getBoard_code());
//		shopRepository.save(shop);
//		log.info(shop);
	}

}
