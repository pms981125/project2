package com.lec.project.shoppingmall.service.magement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.lec.project.shoppingmall.dto.magement.OrderManagementDTO;
import com.lec.project.shoppingmall.dto.magement.RefundRequestDTO;

public interface OrderManagementService {
    // 주문 목록 조회 (필터링 및 페이징)
    Page<OrderManagementDTO> getOrderList(
		@Param("status")String status,
		@Param("startDate")LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate,
		@Param("search") String search,
		Pageable pageable
    );

    // 주문 상세 조회
    OrderManagementDTO getOrderDetails(Long orderId);

    // 주문 상태 변경
    OrderManagementDTO updateOrderStatus(Long orderId, String status);

    // 환불 요청 목록 조회
    List<RefundRequestDTO> getRefundRequests();

    // 환불 요청 처리
    RefundRequestDTO processRefundRequest(
        Long orderId, 
        boolean approve, 
        String managerComment
    );

    // 주문 상태별 통계
    Map<String, Long> getOrderStatusStatistics();
}
