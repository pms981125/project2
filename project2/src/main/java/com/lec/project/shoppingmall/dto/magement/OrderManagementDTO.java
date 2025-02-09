package com.lec.project.shoppingmall.dto.magement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.lec.project.Member;
import com.lec.project.shoppingmall.domain.cart.order.OrderStatus;
import com.lec.project.shoppingmall.domain.cart.order.Ordered;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
        
        // null 체크를 포함한 products 매핑
        List<OrderProductDTO> productDTOs = ordered.getOrderedProducts() != null ?
            ordered.getOrderedProducts().stream()
                .filter(product -> product != null)  // null 상품 필터링
                .map(OrderProductDTO::fromEntity)
                .collect(Collectors.toList()) :
            List.of();  // 비어있는 리스트 반환
        
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
            .orderStatus(ordered.getStatus())
            .products(productDTOs)
            .build();
    }

    // 주문 상태 결정 메서드 (필요에 따라 확장 가능)
    private static String determineOrderStatus(Ordered ordered) {
        // 기본적으로 PENDING으로 시작
        return "PENDING"; 
    }
}