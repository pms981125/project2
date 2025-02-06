package com.lec.project.shoppingmall.dto.magement;

import java.time.LocalDateTime;

import com.lec.project.shoppingmall.domain.payment.kakao.KakaoPaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefundRequestDTO {

	private Long orderId;
	private String customerId;
	private String customerName;
	private int refundAmount;
	private String refundReason;
	private LocalDateTime requestDate;
	private KakaoPaymentStatus status;
}
