package com.lec.project.shoppingmall.dto.payment.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayApproveRequest {
	private String tid;             // 결제 고유번호
	private String partnerOrderId;  // 가맹점 주문번호
	private String partnerUserId;   // 가맹점 회원 ID
	private String pgToken;         // 결제 승인 요청 토큰
}
