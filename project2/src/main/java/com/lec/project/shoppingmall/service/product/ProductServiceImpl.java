package com.lec.project.shoppingmall.service.product;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.lec.project.shoppingmall.domain.product.Product;
import com.lec.project.shoppingmall.dto.product.ProductDTO;
import com.lec.project.shoppingmall.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService{

	private final ModelMapper modelMapper;
	private final ProductRepository productRepository;
	
	@Override
	public String register(ProductDTO productDTO) {
		Product product = modelMapper.map(productDTO, Product.class);
		String product_name = productRepository.save(product).getProduct_name();
		return product_name;
	}

	public Product findById(String productCode) {
	    return productRepository.findById(productCode)
	            .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
	}
}
