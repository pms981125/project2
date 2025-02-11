package com.lec.project.shoppingmall.service.refund;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

import com.lec.project.shoppingmall.domain.cart.order.OrderStatus;
import com.lec.project.shoppingmall.dto.refund.UserRefundRequestDTO;
import com.lec.project.shoppingmall.dto.refund.UserRefundListResponseDTO;
import com.lec.project.shoppingmall.dto.refund.UserRefundDetailResponseDTO;
import com.lec.project.shoppingmall.dto.refund.UserRefundStatusUpdateDTO;

public interface RefundService {
	
    // 환불 요청 생성
	UserRefundDetailResponseDTO createRefund(String memberId, UserRefundRequestDTO refundRequestDTO);
    
    // 환불 상태 업데이트
	UserRefundDetailResponseDTO updateRefundStatus(UserRefundStatusUpdateDTO refundStatusUpdateDTO);
    
    // 환불 상세 조회
	UserRefundDetailResponseDTO getRefundDetails(Long refundId);
    
    // 환불 목록 조회 (필터링, 페이징)
    Page<UserRefundListResponseDTO> getRefundList(
            OrderStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String search,
            Pageable pageable
    );
    
    // 회원별 환불 목록 조회
    Page<UserRefundListResponseDTO> getMemberRefunds(String memberId, Pageable pageable);
}
