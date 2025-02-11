package com.lec.project.shoppingmall.dto.payment.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayReadyRequestDTO {

	private String partnerOrderId;      // 가맹점 주문번호
	private String partnerUserId;       // 가맹점 회원 ID
	private String itemName;            // 상품명
	private int quantity;               // 수량
	private int totalAmount;            // 총 금액
	private int taxFreeAmount;          // 비과세 금액
	private String successUrl;          // 성공 시 redirect URL
	private String cancelUrl;           // 취소 시 redirect URL
	private String failUrl;             // 실패 시 redirect URL
}
