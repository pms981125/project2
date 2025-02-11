package com.lec.project.shoppingmall.controller.payment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayReadyRequest;
import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayReadyResponse;
import com.lec.project.shoppingmall.service.cart.CartService;
import com.lec.project.shoppingmall.service.kakaopay.KakaoPaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/kakao-pay")
@RequiredArgsConstructor
@Log4j2
public class KakaoPaymentApiController {

    private final KakaoPaymentService kakaoPaymentService;
    private final CartService cartService;

    @PostMapping("/ready")
    public ResponseEntity<KakaoPayReadyResponse> readyToPay(
            @RequestBody KakaoPayReadyRequest kakaoPayReadyRequest) {
    	try {
    		log.info("Received payment request: {}", kakaoPayReadyRequest);
    		
            // 결제 금액 검증
    		if(!validatePaymentAmount(kakaoPayReadyRequest)) {
    			log.warn("Invalid payment amount for order: {}", kakaoPayReadyRequest.getPartnerOrderId());
    			return ResponseEntity.badRequest().body(null);
    		}
    		
    		log.info("Payment amount validaated successfully");
    		KakaoPayReadyResponse kakaoPayReadyResponse = kakaoPaymentService.readyToPay(kakaoPayReadyRequest);
    		log.info("Payment ready response: {}", kakaoPayReadyResponse);
    		
    		return ResponseEntity.ok(kakaoPayReadyResponse);
    	} catch (Exception e) {
    		log.error("Payment preparation failed", e);
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    	}
    }
    
    private boolean validatePaymentAmount(KakaoPayReadyRequest kakaoPayReadyRequest) {
        try {
            int actualTotalPrice = cartService.getTotalPrice(kakaoPayReadyRequest.getPartnerUserId());
			log.info("Actual total price: {}, Requested amount: {}", 
					actualTotalPrice, kakaoPayReadyRequest.getTotalAmount());
			
            return actualTotalPrice == kakaoPayReadyRequest.getTotalAmount();
        } catch (Exception e) {
            log.error("Failed to validate payment amount", e);
            return false;
        }
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Payment API error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("결제 처리 중 오류가 발생했습니다.");
    }
}
