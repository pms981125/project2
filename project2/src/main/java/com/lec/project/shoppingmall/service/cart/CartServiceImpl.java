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

		return cartRepository.findByMemberId(memberId).orElseGet(() -> cartRepository.save(Cart.createCart(member)));
	}

	@Override
	public PageResponseDTO<CartListDTO> list(PageRequestDTO pageRequestDTO, String memberid) {
		Cart cart = getOrCreateCart(memberid);
		Pageable pageable = pageRequestDTO.getPageable("id");
		Page<CartProduct> result = cartProductRepository.findByCart(cart, pageable);

		List<CartListDTO> dtoList = result.getContent().stream().map(cartProduct -> {
			CartListDTO dto = modelMapper.map(cartProduct, CartListDTO.class);
			Product product = cartProduct.getProduct();
			dto.setProductName(product.getProductName());
			dto.setProductCode(product.getProductCode());
			dto.setPrice(product.getProductPrice());
			
			ProductImageDTO mainImage = productImageService.getMainImage(product.getProductCode());
			if(mainImage != null) {
				dto.setThumbnailPath(mainImage.getThumbnailPath());
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
		dto.setProductName(cartProduct.getProduct().getProductName());
		dto.setPrice(cartProduct.getProduct().getProductPrice());
		
		ProductImageDTO mainImage = productImageService.getMainImage(cartProduct.getProduct().getProductCode());
		if(mainImage != null) {
			dto.setImgPath(mainImage.getImgPath());
			dto.setThumbnailPath(mainImage.getThumbnailPath());
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

		Product product = cartProduct.getProduct();
	    int currentStock = product.getProductStock();
	    int currentCartQuantity = cartProduct.getCount();
		
	    // 수량 변경에 따른 재고 조정
	    if (count > currentCartQuantity) {
	        // 수량 증가
	        int additionalQuantity = count - currentCartQuantity;
	        if (additionalQuantity > currentStock) {
	            throw new IllegalArgumentException("재고가 부족합니다.");
	        }
	        product.setProductStock(currentStock - additionalQuantity);
	    } else if (count < currentCartQuantity) {
	        // 수량 감소
	        int reducedQuantity = currentCartQuantity - count;
	        product.setProductStock(currentStock + reducedQuantity);
	    }

	    productRepository.save(product);
	    
		cartProduct.setCount(count);
		cartProduct.setTotalPrice(count * cartProduct.getProduct().getProductPrice());

		cartProductRepository.save(cartProduct);
	}

	@Override
	public void remove(Long id, String memberId) {

		CartProduct cartProduct = cartProductRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("장바구니 상품을 찾을 수 없습니다."));

		if (!cartProduct.getCart().getMember().getId().equals(memberId)) {
			throw new IllegalArgumentException("접근 권한이 없습니다.");
		}
		
	    Product product = cartProduct.getProduct();
	    int currentStock = product.getProductStock();
	    int cartQuantity = cartProduct.getCount();
	    
	    product.setProductStock(currentStock + cartQuantity);
	    productRepository.save(product);
		
		cartProductRepository.deleteById(id);
	}

	@Override
	public void removeAll(String memberId, boolean restoreStock) {
		
		log.info("Removing all cart items for member: {}", memberId);
		
		Cart cart = getOrCreateCart(memberId);
		List<CartProduct> cartProducts = cartProductRepository.findByCart(cart);
		
		log.info("Found {} cart items to remove", cartProducts.size());
		
	    // 각 장바구니 상품의 재고 복원
		if(restoreStock) {
		    for (CartProduct cartProduct : cartProducts) {
		        Product product = cartProduct.getProduct();
		        int currentStock = product.getProductStock();
		        int cartQuantity = cartProduct.getCount();
		        
		        log.info("Restoring stock for product {}: current stock {}, cart quantity {}", 
		                  product.getProductCode(), currentStock, cartQuantity);
		        
		        product.setProductStock(currentStock + cartQuantity);
		        productRepository.save(product);
		    }
		}
		
		cartProductRepository.deleteByCart(cart);
	}

	@Override
	public void addToCart(String memberId, String productCode, int count) {
		Cart cart = getOrCreateCart(memberId);
		Product product = productRepository.findById(productCode)
				.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

		// 남은 재고 확인
		int remainingStock = product.getProductStock();
		
		Optional<CartProduct> exsitingProduct = cartProductRepository.findByCartAndProduct(cart, product);
		int currentCartQuantity = exsitingProduct.map(CartProduct::getCount).orElse(0);
		
		int totalRequestedQuantity = currentCartQuantity + count;
		
		if (totalRequestedQuantity > remainingStock) {
			count = remainingStock - currentCartQuantity;
	        if (count <= 0) {
	            throw new IllegalArgumentException("더 이상 상품을 담을 수 없습니다. 재고가 부족합니다.");
	        }
	        
	        throw new IllegalArgumentException(
	        	String.format("요청하신 수량(%d개)은 재고를 초과합니다. %d개만 장바구니에 담겼습니다.", 
	        	totalRequestedQuantity, count)
	        );
		}
		
	    // 상품 재고 감소
	    product.setProductStock(remainingStock - count);
	    productRepository.save(product);
		
		if (exsitingProduct.isPresent()) {
			CartProduct cartProduct = exsitingProduct.get();
			int newCount = cartProduct.getCount() + count;
			cartProduct.setCount(newCount);
			cartProduct.setTotalPrice(newCount * product.getProductPrice());
			cartProductRepository.save(cartProduct);
		} else {
			CartProduct cartProduct = CartProduct.CreateProductCart(cart, product, count,
					count * product.getProductPrice());

			cartProductRepository.save(cartProduct);
		}
	}

	@Override
	public int getTotalPrice(String memberId) {
		Cart cart = getOrCreateCart(memberId);
		return cartProductRepository.findByCart(cart).stream().mapToInt(CartProduct::getTotalPrice).sum();
	}

}
