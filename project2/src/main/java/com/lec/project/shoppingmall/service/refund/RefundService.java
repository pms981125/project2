package com.lec.project.shoppingmall.service.refund;

import java.awt.print.Pageable;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.lec.project.shoppingmall.domain.cart.order.OrderStatus;
import com.lec.project.shoppingmall.domain.refund.RefundListResponseDTO;
import com.lec.project.shoppingmall.domain.refund.RefundResponseDTO;
import com.lec.project.shoppingmall.domain.refund.RefundStatusUpdateDTO;
import com.lec.project.shoppingmall.dto.magement.RefundRequestDTO;

public interface RefundService {
//	
//    // 환불 요청 생성
//    RefundResponseDTO createRefund(String memberId, RefundRequestDTO refundRequestDTO);
//    
//    // 환불 상태 업데이트
//    RefundResponseDTO updateRefundStatus(RefundStatusUpdateDTO refundStatusUpdateDTO);
//    
//    // 환불 상세 조회
//    RefundResponseDTO getRefundDetails(Long refundId);
//    
//    // 환불 목록 조회 (필터링, 페이징)
//    Page<RefundListResponseDTO> getRefundList(
//        OrderStatus status,
//        LocalDateTime startDate,
//        LocalDateTime endDate,
//        String search,
//        Pageable pageable
//    );
//    
//    // 회원별 환불 목록 조회
//    Page<RefundListResponseDTO> getMemberRefunds(String memberId, Pageable pageable);
}
