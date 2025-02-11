package com.lec.project.shoppingmall.service.kakaopay;

import com.lec.project.shoppingmall.domain.payment.kakao.KakaoPayment;
import com.lec.project.shoppingmall.dto.cart.order.OrderSubmitDTO;
import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayApproveRequestDTO;
import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayReadyRequestDTO;
import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayReadyResponseDTO;
import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayRefundRequestDTO;

public interface KakaoPaymentService {
	KakaoPayReadyResponseDTO readyToPay(KakaoPayReadyRequestDTO kakaoPayReadyRequestDTO);
	KakaoPayment approvePayment(KakaoPayApproveRequestDTO kakaoPayApproveRequestDTO, OrderSubmitDTO orderSubmitDTO);
	KakaoPayment refundPayment(KakaoPayRefundRequestDTO kakaoPayRefundRequestDTO);
}