package com.lec.project.shoppingmall.controller.management.api;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lec.project.shoppingmall.dto.magement.OrderManagementDTO;
import com.lec.project.shoppingmall.dto.magement.RefundProcessRequestDTO;
import com.lec.project.shoppingmall.dto.magement.RefundRequestDTO;
import com.lec.project.shoppingmall.service.magement.OrderManagementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/manager/orders")
@RequiredArgsConstructor
@Log4j2
public class OrderManagementRestController {

	private final OrderManagementService orderManagementService;
	
	// 주문상태 변경
	//@PatchMapping: HTTP PATCH 매서드 처리, 리소스의 일부(특정필드)만 수정할 떄 사용
	@PatchMapping("/{orderId}/status")
	public ResponseEntity<OrderManagementDTO> updateOrderStatus(
		@PathVariable Long orderId,
		@RequestBody Map<String, String> payload
	) {
		String status = payload.get("status");
		OrderManagementDTO updateOrder = orderManagementService.updateOrderStatus(orderId, status);
		return ResponseEntity.ok(updateOrder);
	}
	
    // 환불 요청 목록 조회
    @GetMapping("/refunds")
    public ResponseEntity<List<RefundRequestDTO>> getRefundRequests() {
        List<RefundRequestDTO> refundRequests = orderManagementService.getRefundRequests();
        return ResponseEntity.ok(refundRequests);
    }
    
    // 환불 요청 처리
    @PostMapping("/refunds/{orderId}/process")
    public ResponseEntity<RefundRequestDTO> processRefundRequest(
        @PathVariable Long orderId,
        @RequestBody RefundProcessRequestDTO RefundProcessRequest
    ) {
        RefundRequestDTO processedRefund = orderManagementService.processRefundRequest(
            orderId, 
            RefundProcessRequest.isApprove(), 
            RefundProcessRequest.getManagerComment()
        );
        return ResponseEntity.ok(processedRefund);
    }
}
