package com.lec.project.shoppingmall.service.cart.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lec.project.Member;
import com.lec.project.MemberRepository;
import com.lec.project.shoppingmall.domain.cart.order.Ordered;
import com.lec.project.shoppingmall.domain.cart.order.OrderedProduct;
import com.lec.project.shoppingmall.domain.product.Product;
import com.lec.project.shoppingmall.dto.PageRequestDTO;
import com.lec.project.shoppingmall.dto.cart.CartListDTO;
import com.lec.project.shoppingmall.dto.cart.order.OrderSubmitDTO;
import com.lec.project.shoppingmall.repository.OrderedRepository;
import com.lec.project.shoppingmall.repository.ProductRepository;
import com.lec.project.shoppingmall.service.cart.CartService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class OrderServiceImpl implements OrderService{

	private final MemberRepository memberRepository;
	private final CartService cartService;
	private final OrderedRepository orderedRepository;
	private final ProductRepository productRepository;
	
	@Override
	public void createOrder(String memberId, OrderSubmitDTO orderSubmitDTO) {
        // 회원 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        
        //주문 정보 생성
        Ordered order = Ordered.builder()
        		.member(member)
        		//.special_requests(orderSubmitDTO.getSpecial_requests())
        		.specialRequests(orderSubmitDTO.getSpecialRequests())
        		.orderDate(LocalDateTime.now())
        		//.total_amount(orderSubmitDTO.getTotal_amount())
        		.totalAmount(orderSubmitDTO.getTotalAmount())
        		.build();
        
        // 장바구니 조회
        List<CartListDTO> cartItems = cartService.list(new PageRequestDTO(), memberId).getDtoList();
        
        for(CartListDTO item : cartItems) {
        	Product product = productRepository.findById(item.getProductCode())
        			.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        	
        	// 재고 감소 유효성 검사
        	int newStock = product.getProductStock() - item.getCount();
        	if(newStock < 0) {
        		throw new IllegalArgumentException(product.getProductName() + " 상품의 재고가 부족합니다.");
        	}
        	product.setProductStock(newStock);
        	productRepository.save(product);
        }
        
        // 주문 상품 정보 생성
        for(CartListDTO item : cartItems) {
        	Product product = productRepository.findById(item.getProductCode())
        			.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다........."));
        	
        	OrderedProduct orderedProduct = OrderedProduct.builder()
        			.ordered(order)
        			.product(product)
        			.count(item.getCount())
        			.price(item.getPrice())
        			.totalPrice(item.getTotalPrice())
        			.build();
        	
        	order.getOrderedProducts().add(orderedProduct);
        }
        
        // 주문저장
        orderedRepository.save(order);
        
        // 총 구매금액 업데이트
        member.setTotalSpent(member.getTotalSpent() + orderSubmitDTO.getTotalAmount());
        memberRepository.save(member);
        
        // 장바구니 비우기
        cartService.removeAll(memberId);
    }
	
	public void requestRefund(Long orderId) {
		Ordered ordered = orderedRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
		
	    if (!"PENDING".equals(ordered.getStatus()) && !"APPROVED".equals(ordered.getStatus())) {
	        throw new IllegalStateException("환불 요청이 불가능한 상태입니다.");
	    }
	    
	    ordered.setStatus("REFUND_REQUESTED");
	    orderedRepository.save(ordered);
	}
}
