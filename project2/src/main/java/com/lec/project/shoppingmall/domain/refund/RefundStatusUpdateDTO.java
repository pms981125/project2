package com.lec.project.shoppingmall.domain.refund;

import com.lec.project.shoppingmall.domain.cart.order.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundStatusUpdateDTO {

	private Long refundId;
	private OrderStatus newStatus;
}
