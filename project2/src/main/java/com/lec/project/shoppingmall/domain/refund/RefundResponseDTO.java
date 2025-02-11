package com.lec.project.shoppingmall.domain.refund;

import java.time.LocalDateTime;

import com.lec.project.shoppingmall.domain.cart.order.OrderStatus;
import com.lec.project.shoppingmall.domain.cart.order.Refund;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponseDTO {
	private Long id;
	private Long orderId;
	private String customerName;
	private int refundAmount;
	private OrderStatus currentStatus;
	private OrderStatus previousStatus;
	private String refundReason;
	private LocalDateTime requestDate;
	private LocalDateTime processDate;
    
    public static RefundResponseDTO fromEntity(Refund refund) {
        return RefundResponseDTO.builder()
            .id(refund.getId())
            .orderId(refund.getOrder().getId())
            .customerName(refund.getOrder().getCustomerName())
            .refundAmount(refund.getOrder().getTotalAmount())
            .currentStatus(refund.getCurrentStatus())
            .previousStatus(refund.getPreviousStatus())
            .refundReason(refund.getRefundReason())
            .requestDate(refund.getRequestDate())
            .processDate(refund.getProcessDate())
            .build();
    }
}
