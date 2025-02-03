package com.lec.project.shoppingmall.service.kakaopay;

import com.lec.project.shoppingmall.domain.payment.kakao.KakaoPayment;
import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayApproveRequest;
import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayReadyRequest;
import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayReadyResponse;

public interface KakaoPaymentService {
	KakaoPayReadyResponse readyToPay(KakaoPayReadyRequest kakaoPayReadyRequest);
	KakaoPayment approvePayment(KakaoPayApproveRequest kakaoPayApproveRequest);
}
