package com.lec.project.shoppingmall.dto.payment.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayRefundRequestDTO {
	private String tid;
	private int cancelAmount;
	private String cancelReason;
	private String partnerOrderId;
	private String partnerUserId;
}
