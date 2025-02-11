package com.lec.project.shoppingmall.dto.refund;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRefundRequestDTO {

	private Long orderId;
	private String refundReason;
}
