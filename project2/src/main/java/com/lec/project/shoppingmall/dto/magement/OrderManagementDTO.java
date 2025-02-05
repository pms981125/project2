package com.lec.project.shoppingmall.dto.magement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.lec.project.Member;
import com.lec.project.shoppingmall.domain.cart.order.Ordered;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderManagementDTO {
    private Long orderId;
    private String customerId;
    private String customerName;
    private String phoneNumber;
    private String email;
    private String address;
    private String specialRequests;
    private LocalDateTime orderDate;
    private int totalAmount;
    private String orderStatus; // 주문 상태 (PENDING, APPROVED, REJECTED)
    private List<OrderProductDTO> products;

    // Ordered 엔티티로부터 DTO 변환 메서드
    public static OrderManagementDTO fromEntity(Ordered ordered) {
        Member member = ordered.getMember();
        return OrderManagementDTO.builder()
            .orderId(ordered.getId())
            .customerId(member.getId())
            .customerName(member.getName())
            .phoneNumber(member.getPhone())
            .email(member.getEmail())
            .address(member.getDetailedAddress())
            .specialRequests(ordered.getSpecialRequests())
            .orderDate(ordered.getOrderDate())
            .totalAmount(ordered.getTotalAmount())
            .orderStatus(determineOrderStatus(ordered)) // 주문 상태 결정 로직
            .products(ordered.getOrderedProducts().stream()
                .map(OrderProductDTO::fromEntity)
                .collect(Collectors.toList()))
            .build();
    }

    // 주문 상태 결정 메서드 (필요에 따라 확장 가능)
    private static String determineOrderStatus(Ordered ordered) {
        // 기본적으로 PENDING으로 시작
        return "PENDING"; 
    }
}