package com.lec.project.shoppingmall.service.cart;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lec.project.shoppingmall.domain.cart.Cart;
import com.lec.project.shoppingmall.domain.cart.CartProduct;
import com.lec.project.shoppingmall.domain.product.Product;
import com.lec.project.shoppingmall.dto.PageRequestDTO;
import com.lec.project.shoppingmall.dto.PageResponseDTO;
import com.lec.project.shoppingmall.dto.cart.CartListDTO;
import com.lec.project.shoppingmall.repository.CartProductRepository;
import com.lec.project.shoppingmall.repository.CartRepository;
import com.lec.project.shoppingmall.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class CartServiceImpl implements CartService {

	private final CartProductRepository cartProductRepository;
	private final ProductRepository productRepository;
	private final ModelMapper modelMapper;

	@Override
	public PageResponseDTO<CartListDTO> list(PageRequestDTO pageRequestDTO) {
		Pageable pageable = pageRequestDTO.getPageable("id");
		Page<CartProduct> result = cartProductRepository.findAll(pageable);

		List<CartListDTO> dtoList = result.getContent().stream().map(cartProduct -> {
			CartListDTO dto = modelMapper.map(cartProduct, CartListDTO.class);
			Product product = cartProduct.getProduct();
			dto.setProduct_name(product.getProduct_name());
			dto.setProduct_code(product.getProduct_code());
			dto.setPrice(product.getProduct_price());
			return dto;
		}).collect(Collectors.toList());

		return PageResponseDTO.<CartListDTO>withAll().pageRequestDTO(pageRequestDTO).dtoList(dtoList)
				.total((int) result.getTotalElements()).build();
	}

	@Override
	public CartListDTO readOne(Long id) {
		CartProduct cartProduct = cartProductRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("장바구니 상품을 찾을 수 없습니다."));

		CartListDTO dto = modelMapper.map(cartProduct, CartListDTO.class);
		dto.setProduct_name(cartProduct.getProduct().getProduct_name());
		dto.setPrice(cartProduct.getProduct().getProduct_price());

		return dto;
	}

	@Override
	public void modify(Long id, int count) {
		CartProduct cartProduct = cartProductRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("장바구니 상품을 찾을 수 없습니다."));

		cartProduct.setCount(count);
		cartProduct.setTotal_price(count * cartProduct.getProduct().getProduct_price());

		cartProductRepository.save(cartProduct);
	}

	@Override
	public void remove(Long id) {
		cartProductRepository.deleteById(id);
	}

	@Override
	public void removeAll() {
		cartProductRepository.deleteAll();
	}

	@Override
	public void addToCart(String productCode, int count) {

		Product product = productRepository.findById(productCode)
				.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

		List<CartProduct> exsitingGoods = cartProductRepository.findAll();
		Optional<CartProduct> exsitingProduct = exsitingGoods.stream()
				.filter(cp -> cp.getProduct().getProduct_code().equals(productCode)).findFirst();

		if (exsitingProduct.isPresent()) {
			CartProduct cartProduct = exsitingProduct.get();
			int newCount = cartProduct.getCount() + count;
			cartProduct.setCount(newCount);
			cartProduct.setTotal_price(newCount * product.getProduct_price());
			cartProductRepository.save(cartProduct);
		} else {
			CartProduct cartProduct = CartProduct.CreateProductCart(null, product, count,
					count * product.getProduct_price());

			cartProductRepository.save(cartProduct);
		}
	}

	@Override
	public int getTotalPrice() {
		return cartProductRepository.findAll().stream().mapToInt(CartProduct::getTotal_price).sum();
	}

}
