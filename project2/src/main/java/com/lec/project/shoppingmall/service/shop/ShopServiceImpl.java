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
	        .map(shop -> {
	            ShopDTO shopDTO = modelMapper.map(shop, ShopDTO.class);
	            
	            // Null 체크 추가
	            if(shop.getProduct() != null) {
	                shopDTO.setProduct_price(shop.getProduct().getProduct_price());
	                shopDTO.setProduct_category(shop.getProduct().getProduct_category());
	                shopDTO.setProduct_stock(shop.getProduct().getProduct_stock());
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
	
	
	@Override
	public Long register(ShopDTO shopDTO) {
		Product product = productRepository.findById(shopDTO.getProduct_code()).orElseThrow(() -> new IllegalArgumentException("Product not found for code: " + shopDTO.getProduct_code()));
		
		Shop shop= Shop.builder()
					.product_code(shopDTO.getProduct_code())
					.board_title(product.getProduct_name())
					.board_content1(product.getProduct_detail1())  // detail1 저장
		            .board_content2(product.getProduct_detail2())  // detail2 저장
					.build();
		
		return shopRepository.save(shop).getBno();
	}


	@Override
	public ShopDTO readOne(Long bno) {
		Optional<Shop> result = shopRepository.findById(bno);
		Shop shop = result.orElseThrow();
		
		ShopDTO shopDTO = modelMapper.map(shop, ShopDTO.class);
		
		Product product = shop.getProduct();
		
		if(product != null) {
	        shopDTO.setProduct_price(product.getProduct_price());
	        shopDTO.setProduct_category(product.getProduct_category());
	        shopDTO.setProduct_stock(product.getProduct_stock());
		}
		return shopDTO;
	}

	@Override
	public void modify(ShopDTO shopDTO) { // 상품코드만 수정하게

		 Shop shop = shopRepository.findById(shopDTO.getBno()).orElseThrow();
		
		Product product = productRepository.findById(shopDTO.getProduct_code()).orElseThrow();
		
		shop.changeProductCode(shopDTO.getProduct_code());
	    shop.setBoard_title(product.getProduct_name());
	    shop.setBoard_content1(product.getProduct_detail1());
	    shop.setBoard_content2(product.getProduct_detail2());
	    
	    shopRepository.save(shop);
	}

	@Override
	public void remove(Long bno) {
		shopRepository.deleteById(bno);
		
	}


}
