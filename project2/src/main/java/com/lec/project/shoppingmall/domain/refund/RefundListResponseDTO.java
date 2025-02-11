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
public class RefundListResponseDTO {
    
	private Long id;
    private Long orderId;
    private String customerName;
    private int refundAmount;
    private OrderStatus currentStatus;
    private LocalDateTime requestDate;

    public static RefundListResponseDTO fromEntity(Refund refund) {
        return RefundListResponseDTO.builder()
            .id(refund.getId())
            .orderId(refund.getOrder().getId())
            .customerName(refund.getOrder().getCustomerName())
            .refundAmount(refund.getOrder().getTotalAmount())
            .currentStatus(refund.getCurrentStatus())
            .requestDate(refund.getRequestDate())
            .build();
    }
}
