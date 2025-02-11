package com.lec.project.shoppingmall.dto.refund;

import java.time.LocalDateTime;

import com.lec.project.shoppingmall.domain.cart.order.OrderStatus;
import com.lec.project.shoppingmall.domain.refund.Refund;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRefundDetailResponseDTO {
    private Long refundId;
    private Long orderId;
    private int refundAmount;
    private OrderStatus status;
    private LocalDateTime requestDate;
    private LocalDateTime processDate;
    private String refundReason;
    
    // 주문 관련 정보
    private String customerName;
    private String address;
    private String phoneNumber;

    public static UserRefundDetailResponseDTO fromEntity(Refund refund) {
        return UserRefundDetailResponseDTO.builder()
            .refundId(refund.getId())
            .orderId(refund.getOrder().getId())
            .refundAmount(refund.getOrder().getTotalAmount())
            .status(refund.getCurrentStatus())
            .requestDate(refund.getRequestDate())
            .processDate(refund.getProcessDate())
            .refundReason(refund.getRefundReason())
            .customerName(refund.getOrder().getCustomerName())
            .address(refund.getOrder().getAddress())
            .phoneNumber(refund.getOrder().getPhoneNumber())
            .build();
    }
}
