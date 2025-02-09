package com.lec.project.shoppingmall.service.kakaopay;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lec.project.Member;
import com.lec.project.MemberRepository;
import com.lec.project.shoppingmall.domain.cart.order.Ordered;
import com.lec.project.shoppingmall.domain.cart.order.OrderedProduct;
import com.lec.project.shoppingmall.domain.payment.kakao.KakaoPayment;
import com.lec.project.shoppingmall.domain.payment.kakao.KakaoPaymentStatus;
import com.lec.project.shoppingmall.domain.product.Product;
import com.lec.project.shoppingmall.dto.PageRequestDTO;
import com.lec.project.shoppingmall.dto.cart.CartListDTO;
import com.lec.project.shoppingmall.dto.cart.order.OrderSubmitDTO;
import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayApproveRequest;
import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayReadyRequest;
import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayReadyResponse;
import com.lec.project.shoppingmall.repository.KakaoPaymentRepository;
import com.lec.project.shoppingmall.repository.OrderedRepository;
import com.lec.project.shoppingmall.repository.ProductRepository;
import com.lec.project.shoppingmall.service.cart.CartService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class KakaoPaymentServiceImpl implements KakaoPaymentService {

	private static final String KAKAO_PAY_HOST = "https://kapi.kakao.com";
	
	private final WebClient webClient;
	private final KakaoPaymentRepository kakaoPaymentRepository;
	private final MemberRepository memberRepository;
	private final OrderedRepository orderedRepository;
	private final CartService cartService;
	private final ProductRepository productRepository;
	
	@Value("${kakao.pay.admin.key}")
	private String adminKey;
	
	@Override
	public KakaoPayReadyResponse readyToPay(KakaoPayReadyRequest KakaoPayReadyRequest) {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("cid", "TC0ONETIME");     // 가맹점 코드
		parameters.add("partner_order_id", KakaoPayReadyRequest.getPartnerOrderId());
		parameters.add("partner_user_id", KakaoPayReadyRequest.getPartnerUserId());
		parameters.add("item_name", KakaoPayReadyRequest.getItemName());
		parameters.add("quantity", String.valueOf(KakaoPayReadyRequest.getQuantity()));
		parameters.add("total_amount", String.valueOf(KakaoPayReadyRequest.getTotalAmount()));
		parameters.add("tax_free_amount", String.valueOf(KakaoPayReadyRequest.getTaxFreeAmount()));
		
		String baseUrl = "http://localhost:8090";
		parameters.add("approval_url", baseUrl + "/cart/order/kakao/success");
		parameters.add("cancel_url", baseUrl + "/cart/order/kakao/cancel");
		parameters.add("fail_url", baseUrl + "/cart/order/kakao/fail");
		
		log.info("요청 파라미터: {}", parameters);
		log.info("Admin Key: {}", adminKey.substring(0, 8) + "...");
		
	    try {
	        String responseBody = webClient.post()
				.uri(KAKAO_PAY_HOST + "/v1/payment/ready")
				.header("Authorization", "KakaoAK " + adminKey)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData(parameters))
				.retrieve()
				.onStatus(status -> status.is4xxClientError(),
					response -> response.bodyToMono(String.class)
						.flatMap(errorBody -> {
				        	log.error("클라이언트 에러 응답: {} - {}", response.statusCode(), errorBody);
				            return Mono.error(new RuntimeException("카카오페이 API 클라이언트 에러: " + errorBody));
				        }))
				.bodyToMono(String.class)
				.doOnNext(body -> log.info("응답 본문: {}", body))
				.block();

	        log.info("카카오페이 원본 응답: {}", responseBody);

			ObjectMapper objectMapper = new ObjectMapper()
				.registerModule(new JavaTimeModule())
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			    KakaoPayReadyResponse kakaoPayResponse = objectMapper
					.readValue(responseBody, KakaoPayReadyResponse.class);
			    log.info("Mapped response object: {}", kakaoPayResponse);

	        if (kakaoPayResponse.getNextRedirectPcUrl() == null) {
	            log.error("리다이렉트 URL이 null입니다. 응답 전체: {}", responseBody);
	            throw new RuntimeException("카카오페이 응답에 리다이렉트 URL이 없습니다");
	        }

	        log.info("Ready to pay - tid: {}", kakaoPayResponse.getTid());
	        
	        // 결제 정보 저장
	        KakaoPayment Kakaopay = KakaoPayment.builder()
	            .tid(kakaoPayResponse.getTid())
	            .partnerOrderId(KakaoPayReadyRequest.getPartnerOrderId())
	            .partnerUserId(KakaoPayReadyRequest.getPartnerUserId())
	            .totalAmount(KakaoPayReadyRequest.getTotalAmount())
	            .itemName(KakaoPayReadyRequest.getItemName())
	            .quantity(KakaoPayReadyRequest.getQuantity())
	            .status(KakaoPaymentStatus.READY)
	            .build();
	            
	        kakaoPaymentRepository.save(Kakaopay);
	        
	        return kakaoPayResponse;
	    } catch (Exception e) {
	        log.error("Kakao Pay Ready API Error", e);
	        throw new RuntimeException("결제 준비 중 오류가 발생했습니다.");
	    }
	}
	
	@Override
	public KakaoPayment approvePayment(
			KakaoPayApproveRequest kakaoPayApproveRequest,
			OrderSubmitDTO orderSubmitDTO
	) {
		try {
	        log.info("Starting payment approval process");
	        log.info("OrderSubmitDTO received: {}", orderSubmitDTO);
			
			// 결제 승인 요청 로직
		    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		    parameters.add("cid", "TC0ONETIME");
		    parameters.add("tid", kakaoPayApproveRequest.getTid());
		    parameters.add("partner_order_id", kakaoPayApproveRequest.getPartnerOrderId());
		    parameters.add("partner_user_id", kakaoPayApproveRequest.getPartnerUserId());
		    parameters.add("pg_token", kakaoPayApproveRequest.getPgToken());
		    
		    log.info("Payment approved from Kakao");
		    
		    // 카카오페이 승인 요청
		    Object approveResponse = webClient.post()
				.uri(KAKAO_PAY_HOST + "/v1/payment/approve")
				.header("Authorization", "KakaoAK " + adminKey)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData(parameters))
				.retrieve()
				.onStatus(status -> status.is4xxClientError(), 
				    response -> response.bodyToMono(String.class)
				        .flatMap(errorBody -> Mono.error(new RuntimeException("승인 요청 클라이언트 오류: " + errorBody))))
				.onStatus(status -> status.is5xxServerError(), 
				    response -> response.bodyToMono(String.class)
				        .flatMap(errorBody -> Mono.error(new RuntimeException("승인 요청 서버 오류: " + errorBody))))
				.bodyToMono(Object.class)
				.block();
		        
		    // 결제 정보 업데이트
		    KakaoPayment Kakaopayment = kakaoPaymentRepository.findByTid(kakaoPayApproveRequest.getTid())
		        .orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다."));

		    // 회원 정보 조회
		    Member member = memberRepository.findById(kakaoPayApproveRequest.getPartnerUserId())
	        	.orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
		    
		    
		    
		    // 주문 정보 생성
		    Ordered ordered = Ordered.builder()
				.member(member)
				.customerName(orderSubmitDTO.getCustomerName())
				.phoneNumber(orderSubmitDTO.getPhoneNumber())
				.address(orderSubmitDTO.getAddress())
				.specialRequests(orderSubmitDTO.getSpecialRequests())
				.orderDate(LocalDateTime.now())
				.totalAmount(Kakaopayment.getTotalAmount())
				.status("APPROVED")
				.build();
		    
		    log.info("Creating Ordered entity with data: {}", ordered);
		    
		 // 장바구니 조회 및 주문상품 생성
	        List<CartListDTO> cartItems = cartService.list(new PageRequestDTO(), member.getId()).getDtoList();
	        for(CartListDTO item : cartItems) {
	            Product product = productRepository.findById(item.getProductCode())
	                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
	                
	            OrderedProduct orderedProduct = OrderedProduct.builder()
	                .ordered(ordered)
	                .product(product)
	                .count(item.getCount())
	                .price(item.getPrice())
	                .totalPrice(item.getTotalPrice())
	                .build();
	                
	            ordered.getOrderedProducts().add(orderedProduct);
	        }
		    
	        
	        // 주문 정보 생성 및 저장
	        orderedRepository.save(ordered);
	        
	        log.info("Saved Ordered entity: {}", ordered);
	        
	        Kakaopayment.setOrder(ordered);
	        Kakaopayment.setStatus(KakaoPaymentStatus.APPROVED);
	        Kakaopayment.setApprovedAt(LocalDateTime.now());
	        
	        // 장바구니 비우기
	        cartService.removeAll(member.getId());
	        
	        log.info("Completing payment process");
	        
	        
	        
	        return kakaoPaymentRepository.save(Kakaopayment);
	        
	    } catch (Exception e) {
	        log.error("결제 승인 중 오류 발생", e);
	        throw new RuntimeException("결제 승인에 실패했습니다.", e);
	    }
	}
}
