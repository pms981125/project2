package com.lec.project.shoppingmall.service.cart;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lec.project.Member;
import com.lec.project.MemberRepository;
import com.lec.project.shoppingmall.domain.cart.Cart;
import com.lec.project.shoppingmall.domain.cart.CartProduct;
import com.lec.project.shoppingmall.domain.product.Product;
import com.lec.project.shoppingmall.dto.PageRequestDTO;
import com.lec.project.shoppingmall.dto.PageResponseDTO;
import com.lec.project.shoppingmall.dto.cart.CartListDTO;
import com.lec.project.shoppingmall.dto.product.image.ProductImageDTO;
import com.lec.project.shoppingmall.repository.CartProductRepository;
import com.lec.project.shoppingmall.repository.CartRepository;
import com.lec.project.shoppingmall.repository.ProductRepository;
import com.lec.project.shoppingmall.service.product.image.ProductImageService;

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
	private final CartRepository cartRepository;
	private final MemberRepository memberRepository;	
	private final ModelMapper modelMapper;
	private final ProductImageService productImageService;

	private Cart getOrCreateCart(String memberId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다........."));

		return cartRepository.findByMember_id(memberId).orElseGet(() -> cartRepository.save(Cart.createCart(member)));
	}

	@Override
	public PageResponseDTO<CartListDTO> list(PageRequestDTO pageRequestDTO, String memberid) {
		Cart cart = getOrCreateCart(memberid);
		Pageable pageable = pageRequestDTO.getPageable("id");
		Page<CartProduct> result = cartProductRepository.findByCart(cart, pageable);

		List<CartListDTO> dtoList = result.getContent().stream().map(cartProduct -> {
			CartListDTO dto = modelMapper.map(cartProduct, CartListDTO.class);
			Product product = cartProduct.getProduct();
			dto.setProduct_name(product.getProduct_name());
			dto.setProduct_code(product.getProduct_code());
			dto.setPrice(product.getProduct_price());
			
			ProductImageDTO mainImage = productImageService.getMainImage(product.getProduct_code());
			if(mainImage != null) {
				dto.setThumbnail_path(mainImage.getThumbnail_path());
			}
			
			
			return dto;
		}).collect(Collectors.toList());

		return PageResponseDTO.<CartListDTO>withAll().pageRequestDTO(pageRequestDTO).dtoList(dtoList)
				.total((int) result.getTotalElements()).build();
	}

	@Override
	public CartListDTO readOne(Long id, String memberId) {
		CartProduct cartProduct = cartProductRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("장바구니 상품을 찾을 수 없습니다."));

		// 내 장바구니인지 확인
		if (!cartProduct.getCart().getMember().getId().equals(memberId)) {
			throw new IllegalArgumentException("접근 권한이 없습니다.");
		}

		CartListDTO dto = modelMapper.map(cartProduct, CartListDTO.class);
		dto.setProduct_name(cartProduct.getProduct().getProduct_name());
		dto.setPrice(cartProduct.getProduct().getProduct_price());
		
		ProductImageDTO mainImage = productImageService.getMainImage(cartProduct.getProduct().getProduct_code());
		if(mainImage != null) {
			dto.setImg_path(mainImage.getImg_path());
			dto.setThumbnail_path(mainImage.getThumbnail_path());
		}

		return dto;
	}

	@Override
	public void modify(Long id, int count, String memberId) {
		CartProduct cartProduct = cartProductRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("장바구니 상품을 찾을 수 없습니다."));

		if (!cartProduct.getCart().getMember().getId().equals(memberId)) {
			throw new IllegalArgumentException("접근 권한이 없습니다.");
		}

		cartProduct.setCount(count);
		cartProduct.setTotal_price(count * cartProduct.getProduct().getProduct_price());

		cartProductRepository.save(cartProduct);
	}

	@Override
	public void remove(Long id, String memberId) {

		CartProduct cartProduct = cartProductRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("장바구니 상품을 찾을 수 없습니다."));

		if (!cartProduct.getCart().getMember().getId().equals(memberId)) {
			throw new IllegalArgumentException("접근 권한이 없습니다.");
		}
		cartProductRepository.deleteById(id);
	}

	@Override
	public void removeAll(String memberId) {
		Cart cart = getOrCreateCart(memberId);
		cartProductRepository.deleteByCart(cart);
	}

	@Override
	public void addToCart(String memberId, String productCode, int count) {
		Cart cart = getOrCreateCart(memberId);
		Product product = productRepository.findById(productCode)
				.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

		Optional<CartProduct> exsitingProduct = cartProductRepository.findByCartAndProduct(cart, product);

		if (exsitingProduct.isPresent()) {
			CartProduct cartProduct = exsitingProduct.get();
			int newCount = cartProduct.getCount() + count;
			cartProduct.setCount(newCount);
			cartProduct.setTotal_price(newCount * product.getProduct_price());
			cartProductRepository.save(cartProduct);
		} else {
			CartProduct cartProduct = CartProduct.CreateProductCart(cart, product, count,
					count * product.getProduct_price());

			cartProductRepository.save(cartProduct);
		}
	}

	@Override
	public int getTotalPrice(String memberId) {
		Cart cart = getOrCreateCart(memberId);
		return cartProductRepository.findByCart(cart).stream().mapToInt(CartProduct::getTotal_price).sum();
	}

}
