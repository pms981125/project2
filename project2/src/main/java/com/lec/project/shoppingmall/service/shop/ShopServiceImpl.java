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
import com.lec.project.shoppingmall.dto.shop.ShopDTO;
import com.lec.project.shoppingmall.repository.ProductRepository;
import com.lec.project.shoppingmall.repository.ShopRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class ShopServiceImpl implements ShopService{
	
	private final ModelMapper modelMapper;
	private final ProductRepository productRepository;
	private final ShopRepository shopRepository;

	@Override
	public PageResponseDTO<ShopDTO> list(PageRequestDTO pageRequestDTO) {
		
		String keyword = pageRequestDTO.getKeyword();
		Pageable pageable = pageRequestDTO.getPageable("bno");
		
		Page<Shop> result = shopRepository.searchAllImpl(keyword, pageable);
		List<ShopDTO> dtoList = result.getContent()
									.stream()
									.map(shop -> {ShopDTO shopDTO = modelMapper.map(shop, ShopDTO.class);
									
									log.info("Shop boardCode: {}", shop.getBoardCode());
									
									if (shop.getBoardCode() != null) {
						                   Optional<Product> productOptional = productRepository.findById(shop.getBoardCode());
						                   
						                   if (productOptional.isPresent()) {
						                       Product product = productOptional.get();
						                       
						                       shopDTO.setProduct_price(product.getProduct_price());
						                       shopDTO.setProduct_category(product.getProduct_category());
						                       shopDTO.setProduct_stock(product.getProduct_stock());
						                   } else {
						                       log.warn("No product found for boardCode: {}", shop.getBoardCode());
						                   }
						               }
									return shopDTO;
									
									})
									.collect(Collectors.toList());
		
		return PageResponseDTO.<ShopDTO>withAll()
		        .pageRequestDTO(pageRequestDTO)
		        .dtoList(dtoList)
		        .total((int)result.getTotalElements())
		        .build();
	}
	
	// board만 등록
//	@Override
//	public Long register(ShopDTO shopDTO) {
//		Shop shop = modelMapper.map(shopDTO, Shop.class);
//		Long bno = shopRepository.save(shop).getBno();
//		return bno;
//	}
	
	@Override
	public Long register(ShopDTO shopDTO) {
		Product product = productRepository.findById(shopDTO.getBoardCode()).orElseThrow();
		
		Shop shop= Shop.builder()
					.boardCode(shopDTO.getBoardCode())
					.board_title(product.getProduct_name())
					.board_content(combineContent(product))
					.build();
		
		return shopRepository.save(shop).getBno();
	}

	private String combineContent(Product product) {
		return String.format(
				"*상품명: %s\n*가격: %d원\n*카테고리: %s\n*재고량: %d개\n*상품설명1: %s\n*상품설명2: %s"
				,product.getProduct_name()
				,product.getProduct_price()
				,product.getProduct_category()
				,product.getProduct_stock()
				,product.getProduct_detail1()
				,product.getProduct_detail2()
				);
	}

	@Override
	public ShopDTO readOne(Long bno) {
		Optional<Shop> result = shopRepository.findById(bno);
		Shop shop = result.orElseThrow();
		ShopDTO shopDTO = modelMapper.map(shop, ShopDTO.class);
		return shopDTO;
	}

	//수정기능 product로 옮김으로 인한 일단 주석처리
	@Override
	public void modify(ShopDTO shopDTO) { // 상품코드만 수정하게
//
//		Optional<Shop> result = shopRepository.findById(shopDTO.getBno());
//		Shop shop = result.orElseThrow();
//		shop.changeCode(shopDTO.getBoard_code());
	}

	@Override
	public void remove(Long bno) {
		shopRepository.deleteById(bno);
		
	}


}
