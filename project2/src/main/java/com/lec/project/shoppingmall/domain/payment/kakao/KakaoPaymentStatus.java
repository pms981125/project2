package com.lec.project.shoppingmall.domain.payment.kakao;

public enum KakaoPaymentStatus {
	READY,				// 결제 준비
	APPROVED,			// 결제 승인
	CANCELLED,			// 결제 취소
	FAILED,				// 결제 실패
	REFUND_REQUESTED	// 환불 요청
}
