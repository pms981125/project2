package com.lec.project.shoppingmall.service.kakaopay;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.lec.project.shoppingmall.domain.payment.kakao.KakaoPayment;
import com.lec.project.shoppingmall.domain.payment.kakao.KakaoPaymentStatus;
import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayApproveRequest;
import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayReadyRequest;
import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayReadyResponse;
import com.lec.project.shoppingmall.repository.KakaoPaymentRepository;

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
	
	@Value("${kakao.pay.admin.key}")
	private String adminKey;
	
	@Override
	public KakaoPayReadyResponse readyToPay(KakaoPayReadyRequest KakaoPayReadyRequest) {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("cid", "TC0ONETIME");		// 가맹점 코드
		parameters.add("partner_order_id", KakaoPayReadyRequest.getPartnerOrderId());
		parameters.add("partner_user_id", KakaoPayReadyRequest.getPartnerUserId());
		parameters.add("item_name", KakaoPayReadyRequest.getItemName());
		parameters.add("quantity", String.valueOf(KakaoPayReadyRequest.getQuantity()));
		parameters.add("total_amount", String.valueOf(KakaoPayReadyRequest.getTotalAmount()));
		parameters.add("tax_free_amount", String.valueOf(KakaoPayReadyRequest.getTaxFreeAmount()));
		parameters.add("approval_url", KakaoPayReadyRequest.getSuccessUrl());
		parameters.add("cancel_url", KakaoPayReadyRequest.getCancelUrl());
		parameters.add("fail_url", KakaoPayReadyRequest.getFailUrl());
		
		log.info("Kakao Pay Request Params: {}", parameters);
		
		try {
			KakaoPayReadyResponse kakaoPayResponse = webClient.post()
				.uri(KAKAO_PAY_HOST + "/v1/payment/ready")
				.header("Authorization", "KakaoAK " + adminKey)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData(parameters))
				.retrieve()
				.onStatus(status -> status.is4xxClientError(), 
					response -> response.bodyToMono(String.class)
				    	.flatMap(errorBody -> {
				        	log.error("Kakao Pay Client Error: {}", errorBody);
				            return Mono.error(new RuntimeException("카카오페이 클라이언트 오류: " + errorBody));
				        }))
				.onStatus(status -> status.is5xxServerError(), 
				    response -> response.bodyToMono(String.class)
				    	.flatMap(errorBody -> {
				        	log.error("Kakao Pay Server Error: {}", errorBody);
				        	return Mono.error(new RuntimeException("카카오페이 서버 오류: " + errorBody));
				        }))
			        .bodyToMono(KakaoPayReadyResponse.class)
			        .block();
			
			log.info("Kakao Pay Response: {}", kakaoPayResponse);

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
	public KakaoPayment approvePayment(KakaoPayApproveRequest kakaoPayApproveRequest) {
		try {
			// 결제 승인 요청 로직
		    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		    parameters.add("cid", "TC0ONETIME");
		    parameters.add("tid", kakaoPayApproveRequest.getTid());
		    parameters.add("partner_order_id", kakaoPayApproveRequest.getPartnerOrderId());
		    parameters.add("partner_user_id", kakaoPayApproveRequest.getPartnerUserId());
		    parameters.add("pg_token", kakaoPayApproveRequest.getPgToken());
		    
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
		        
		    Kakaopayment.setStatus(KakaoPaymentStatus.APPROVED);
		    Kakaopayment.setApprovedAt(LocalDateTime.now());
		    
		    return kakaoPaymentRepository.save(Kakaopayment);
		} catch (Exception e) {
			log.error("결제 승인 중 오류 발생", e);
			throw new RuntimeException("결제 승인에 실패했습니다.", e);
		}
	}
}
