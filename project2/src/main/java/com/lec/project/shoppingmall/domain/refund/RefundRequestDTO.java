package com.lec.project.shoppingmall.domain.refund;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequestDTO {

	private Long orderId;
	private String refundReason;
}
