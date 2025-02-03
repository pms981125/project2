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
            @RequestBody KakaoPayReadyRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
    	try {
            // 사용자 검증
            if (!request.getPartnerUserId().equals(userDetails.getUsername())) {
                log.warn("User ID mismatch: {} vs {}", request.getPartnerUserId(), userDetails.getUsername());
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
            }

            // 결제 금액 검증 로직 추가
            if (!validatePaymentAmount(request)) {
                log.warn("Invalid payment amount for order: {}", request.getPartnerOrderId());
                return ResponseEntity.badRequest()
                    .body(null);
            }

            KakaoPayReadyResponse response = kakaoPaymentService.readyToPay(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Payment preparation failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }
    
    private boolean validatePaymentAmount(KakaoPayReadyRequest request) {
        try {
            int actualTotalPrice = cartService.getTotalPrice(request.getPartnerUserId());
            if (actualTotalPrice != request.getTotalAmount()) {
                log.warn("Payment amount mismatch. Expected: {}, Actual: {}", 
                    actualTotalPrice, request.getTotalAmount());
                return false;
            }
            return true;
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
