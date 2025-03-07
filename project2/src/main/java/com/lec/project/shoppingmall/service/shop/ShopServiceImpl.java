package com.lec.project.shoppingmall.service.shop;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lec.project.shoppingmall.domain.product.Product;
import com.lec.project.shoppingmall.domain.shop.Shop;
import com.lec.project.shoppingmall.dto.PageRequestDTO;
import com.lec.project.shoppingmall.dto.PageResponseDTO;
import com.lec.project.shoppingmall.dto.product.image.ProductImageDTO;
import com.lec.project.shoppingmall.dto.shop.ShopDTO;
import com.lec.project.shoppingmall.repository.ProductRepository;
import com.lec.project.shoppingmall.repository.ShopRepository;
import com.lec.project.shoppingmall.service.product.image.ProductImageService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class ShopServiceImpl implements ShopService {

	private final ProductImageService productImageService;

	private final ProductRepository productRepository;
	private final ShopRepository shopRepository;

	private final ModelMapper modelMapper;

	@Override
	public PageResponseDTO<ShopDTO> list(PageRequestDTO pageRequestDTO, String keyword, String category) {
		
		Pageable pageable = pageRequestDTO.getPageable("bno");

		log.info("Service - Category: {}, Keyword: {}", category, keyword);
		Page<Shop> result = shopRepository.searchAllImpl(keyword, category, pageable);

		List<ShopDTO> dtoList = result.getContent().stream().map(shop -> {
			ShopDTO shopDTO = modelMapper.map(shop, ShopDTO.class);

			
			// Null 체크 추가
			if (shop.getProduct() != null) {
				shopDTO.setProductPrice(shop.getProduct().getProductPrice());
				shopDTO.setProductCategory(shop.getProduct().getProductCategory());
				shopDTO.setProductStock(shop.getProduct().getProductStock());

				ProductImageDTO mainImage = productImageService.getMainImage(shop.getProductCode());
				if (mainImage != null) {
					shopDTO.setImgPath(mainImage.getImgPath());
					shopDTO.setThumbnailPath(mainImage.getThumbnailPath());
				}

				
				
			}
			return shopDTO;
		}).collect(Collectors.toList());
		
		return PageResponseDTO.<ShopDTO>withAll().pageRequestDTO(pageRequestDTO).dtoList(dtoList)
				.total((int) result.getTotalElements()).build();
	}

	@Override
	public Long register(ShopDTO shopDTO) {
		// product_code 존재 확인
		if (shopRepository.existsByProductCode(shopDTO.getProductCode())) {
			throw new IllegalArgumentException("이미 등록된 product_code입니다.");
		}

		Product product = productRepository.findById(shopDTO.getProductCode()).orElseThrow(
				() -> new IllegalArgumentException("Product not found for code: " + shopDTO.getProductCode()));

		Shop shop = Shop.builder()
				.productCode(shopDTO.getProductCode())
				.boardTitle(product.getProductName())
				.firstBoardContent(product.getFirstProductDetail()) // detail1 저장
				.secondBoardContent(product.getSecondProductDetail()) // detail2 저장
				.build();

		return shopRepository.save(shop).getBno();
	}

	@Override
	public ShopDTO readOne(Long bno) {
		Optional<Shop> result = shopRepository.findById(bno);
		Shop shop = result.orElseThrow();
		ShopDTO shopDTO = modelMapper.map(shop, ShopDTO.class);

		Product product = shop.getProduct();

		if (product != null) {
			shopDTO.setProductPrice(product.getProductPrice());
			shopDTO.setProductCategory(product.getProductCategory());
			shopDTO.setProductStock(product.getProductStock());

			ProductImageDTO mainImage = productImageService.getMainImage(shop.getProductCode());
			if (mainImage != null) {
				shopDTO.setImgPath(mainImage.getImgPath());
				shopDTO.setThumbnailPath(mainImage.getThumbnailPath());
			}
		}
		return shopDTO;
	}
	
	public ShopDTO readByProductCode(String productCode) {
		Shop shop = shopRepository.findByProductCode(productCode)
				.orElseThrow(()-> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));
		return modelMapper.map(shop, ShopDTO.class);
	}

	// 수정
	@Override
	public void modify(ShopDTO shopDTO) { // 상품코드로만 수정하게

		// list의 shop에서 조회
		Shop existsShop = shopRepository.findById(shopDTO.getBno())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

		// 기존 product_code와 새로운 product_code가 다른 경우에만 중복 체크
		if (!existsShop.getProductCode().equals(shopDTO.getProductCode())) {
			if (shopRepository.existsByProductCode(shopDTO.getProductCode())) {
				throw new IllegalArgumentException("이미 등록된 상품 코드입니다.");
			}
		}

		// 새로운 Product 조회
		Product product = productRepository.findById(shopDTO.getProductCode())
				.orElseThrow(() -> new IllegalArgumentException("해당 상품 코드의 상품이 존재하지 않습니다."));

		// 재고 수정
		product.setProductStock(shopDTO.getProductStock());
		
		//shop에서 수정된 가격과 내용 업데이트
		product	.setProductPrice(shopDTO.getProductPrice());
		product.setFirstProductDetail(shopDTO.getFirstBoardContent());
		product.setSecondProductDetail(shopDTO.getSecondBoardContent());
		
		//shop의 내용 업데이트
		existsShop.changeProductCode(shopDTO.getProductCode());
		existsShop.changeTitle(product.getProductName());
		existsShop.changeFirstDetail(shopDTO.getFirstBoardContent());
		existsShop.changeSecondDetail(shopDTO.getSecondBoardContent());

		productRepository.save(product);
		shopRepository.save(existsShop);
	}

	@Override
	public void remove(Long bno) {
		shopRepository.deleteById(bno);

	}

}
